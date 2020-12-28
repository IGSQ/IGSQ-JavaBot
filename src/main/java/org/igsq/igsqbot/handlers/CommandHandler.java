package org.igsq.igsqbot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
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

public abstract class CommandHandler
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
		final List<String> args = Arrays.stream(event.getMessage().getContentRaw().split(" ")).collect(Collectors.toList());
		final MessageChannel channel = event.getChannel();
		args.remove(0);

		if(event.getAuthor().isBot() || event.isWebhookMessage()) return;
		if(event.isFromGuild())
		{
			final JDA jda = event.getJDA();
			final Guild guild = event.getGuild();
			final String prefix = new GuildConfig(guild, jda).getGuildPrefix();
			final String selfID = jda.getSelfUser().getId();
			final String messageContent = event.getMessage().getContentRaw();

			final String content;
			final String issuedCommand;
			final Command cmd;

			if(messageContent.startsWith(prefix))
			{
				content = messageContent.substring(prefix.length()).trim();
				issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
				if(!issuedCommand.isEmpty())
				{
					cmd = COMMAND_MAP.get(issuedCommand.toLowerCase());
				}
				else
				{
					return;
				}

			}
			else if(messageContent.startsWith("<@" + selfID + ">") || messageContent.startsWith("<@!" + selfID + ">"))
			{
				content = messageContent.substring(messageContent.indexOf(">") + 1).trim();
				issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
				if(!issuedCommand.isEmpty())
				{
					cmd = COMMAND_MAP.get(issuedCommand.toLowerCase());
				}
				else
				{
					return;
				}
			}
			else
			{
				return;
			}

			if(guild.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
			{
				event.getMessage().delete().queue(null, null);
			}
			if(cmd == null)
			{
				EmbedUtils.sendError(channel, "The command `" + issuedCommand + "` was not found.");
				return;
			}
			else if(cmd.isDisabled())
			{
				EmbedUtils.sendDisabledError(channel, cmd);
				return;
			}
			else if(!cmd.canExecute(new CommandContext(event)))
			{
				EmbedUtils.sendExecutionError(channel, cmd);
				return;
			}

			commandExecutor.submit(() -> cmd.execute(args, new CommandContext(event)));
		}
		else
		{
			final String prefix = Constants.DEFAULT_BOT_PREFIX;
			if(event.getMessage().getContentRaw().startsWith(prefix))
			{
				final String content = event.getMessage().getContentRaw().substring(prefix.length());
				final String issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
				final Command cmd = COMMAND_MAP.get(issuedCommand);

				if(cmd == null)
				{
					EmbedUtils.sendError(channel, "The command `" + issuedCommand + "` was not found.");
				}
				else if(cmd.isDisabled())
				{
					EmbedUtils.sendDisabledError(channel, cmd);
				}
				else if(cmd.isGuildOnly())
				{
					EmbedUtils.sendError(channel, "This command requires execution in a guild.");
				}
				else
				{
					commandExecutor.submit(() -> cmd.execute(args, new CommandContext(event)));
				}
			}
		}
	}

	public static void close()
	{
		commandExecutor.shutdown();
	}
}
