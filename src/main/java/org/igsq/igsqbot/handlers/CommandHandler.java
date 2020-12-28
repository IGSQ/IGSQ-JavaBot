package org.igsq.igsqbot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.yaml.BotConfig;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CommandHandler
{
	public static final String COMMAND_PACKAGE = "org.igsq.igsqbot.commands";
	private static final ClassGraph CLASS_GRAPH = new ClassGraph().acceptPackages(COMMAND_PACKAGE);
	private static final ExecutorService commandExecutor = Executors.newFixedThreadPool(5);
	public static final Map<String, Command> COMMAND_MAP;

	private CommandHandler()
	{
		//Overrides the default, public, constructor
	}

	static
	{
		Map<String, Command> COMMANDS = new HashMap<>();
		try(ScanResult result = CLASS_GRAPH.scan())
		{
			for(ClassInfo cls : result.getAllClasses())
			{
				Command cmd = (Command) cls.loadClass().getDeclaredConstructor().newInstance();
				COMMANDS.put(cmd.getName(), cmd);
				for(String alias : cmd.getAliases()) COMMANDS.put(alias, cmd);
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			System.exit(1);
		}
		COMMAND_MAP = Collections.unmodifiableMap(COMMANDS);
	}

	public static Map<String, Command> getCommandMap()
	{
		return COMMAND_MAP;
	}

	public static void handle(MessageReceivedEvent event)
	{
		if(event.getAuthor().isBot() || event.isWebhookMessage())
		{
			return;
		}

		final List<String> args = Arrays.stream(event.getMessage().getContentRaw().split(" ")).collect(Collectors.toList());
		final String messageContent = event.getMessage().getContentRaw();
		final JDA jda = event.getJDA();
		final MessageChannel channel = event.getChannel();
		final String selfID = jda.getSelfUser().getId();
		final String commandText;
		final String content;
		final Command cmd;

		final boolean startsWithId = messageContent.startsWith("<@" + selfID + ">") || messageContent.startsWith("<@!" + selfID + ">");
		final String idTrimmed = messageContent.substring(messageContent.indexOf(">") + 1).trim();

		if(event.isFromGuild())
		{
			final Guild guild = event.getGuild();
			if(startsWithId)
			{
				content = idTrimmed;
			}
			else if(messageContent.startsWith(new GuildConfig(guild.getId(), jda).getGuildPrefix()))
			{
				final String prefix  = new GuildConfig(guild.getId(), jda).getGuildPrefix();
				content = messageContent.substring(prefix.length()).trim();
				if(guild.getSelfMember().hasPermission((GuildChannel) channel, Permission.MESSAGE_MANAGE))
				{
					event.getMessage().delete().queue(null, error -> {});
				}
			}
			else
			{
				return;
			}
		}
		else
		{
			final String prefix  = Constants.DEFAULT_BOT_PREFIX;
			if(startsWithId)
			{
				content = idTrimmed;
			}
			else if(messageContent.startsWith(prefix))
			{
				content = messageContent.substring(prefix.length()).trim();
			}
			else
			{
				return;
			}
		}

		commandText = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
		if(!commandText.isEmpty())
		{
			cmd = COMMAND_MAP.get(commandText.toLowerCase());
			if(cmd == null)
			{
				EmbedUtils.sendError(channel, "The command `" + commandText + "` was not found.");
			}
			else if(cmd.isDisabled())
			{
				EmbedUtils.sendDisabledError(channel, cmd);
			}
			else if(cmd.isGuildOnly() && !event.isFromGuild())
			{
				EmbedUtils.sendError(channel, "This command requires execution in a server.");
			}
			else if(!cmd.canExecute(new CommandContext(event)))
			{
				EmbedUtils.sendExecutionError(channel, cmd);
			}
			else
			{
				args.remove(0);
				commandExecutor.submit(() -> cmd.execute(args, new CommandContext(event)));
			}
		}
	}

	public static void close()
	{
		commandExecutor.shutdown();
	}
}
