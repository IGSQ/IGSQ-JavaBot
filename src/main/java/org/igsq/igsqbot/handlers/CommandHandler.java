package org.igsq.igsqbot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.util.EmbedUtils;

public class CommandHandler
{
	public static final String COMMAND_PACKAGE = "org.igsq.igsqbot.commands.commands";

	private final ClassGraph classGraph = new ClassGraph().acceptPackages(COMMAND_PACKAGE);
	private final Map<String, Command> commandMap;
	private final IGSQBot igsqBot;

	public CommandHandler(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		commandMap = loadCommands();
	}

	private Map<String, Command> loadCommands()
	{
		Map<String, Command> commands = new HashMap<>();
		try(ScanResult result = classGraph.scan())
		{
			for(ClassInfo cls : result.getAllClasses())
			{
				Constructor<?>[] constructors = cls.loadClass().getDeclaredConstructors();
				if(constructors.length == 0)
				{
					igsqBot.getLogger().warn("No valid constructors found for Command class (" + cls.getSimpleName() + ")!");
					continue;
				}
				if(constructors[0].getParameterCount() > 0)
				{
					continue;
				}
				Object instance = constructors[0].newInstance();
				if(!(instance instanceof Command))
				{
					igsqBot.getLogger().warn("Non Command class (" + cls.getSimpleName() + ") found in commands package!");
					continue;
				}
				Command cmd = (Command) instance;
				commands.put(cmd.getName(), cmd);
				for(String alias : cmd.getAliases()) commands.put(alias, cmd);
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			System.exit(1);
		}
		return Collections.unmodifiableMap(commands);
	}

	public Map<String, Command> getCommandMap()
	{
		return commandMap;
	}

	public void handle(MessageReceivedEvent event)
	{
		if(event.getAuthor().isBot() || event.isWebhookMessage())
		{
			return;
		}

		Message referencedMessage = event.getMessage().getReferencedMessage();

		if(referencedMessage != null && referencedMessage.getAuthor().equals(igsqBot.getSelfUser()))
		{
			return;
		}

		List<String> args = Arrays.stream(event.getMessage().getContentRaw().split("\\s+")).collect(Collectors.toList());
		String messageContent = event.getMessage().getContentRaw();
		JDA jda = event.getJDA();
		MessageChannel channel = event.getChannel();
		String selfID = jda.getSelfUser().getId();
		String commandText;
		String content;
		Command cmd;
		Guild guild = event.isFromGuild() ? event.getGuild() : null;

		boolean startsWithId = messageContent.startsWith("<@" + selfID + ">") || messageContent.startsWith("<@!" + selfID + ">");
		boolean startWithIdSpaced = messageContent.startsWith("<@" + selfID + "> ") || messageContent.startsWith("<@!" + selfID + "> ");
		String idTrimmed = messageContent.substring(messageContent.indexOf(">") + 1).trim();
		String prefix = Constants.DEFAULT_BOT_PREFIX;

		if(event.isFromGuild())
		{
			if(startsWithId)
			{
				content = idTrimmed;
			}
			else if(messageContent.startsWith(new GuildConfig(guild.getIdLong(), igsqBot).getPrefix()))
			{
				prefix = new GuildConfig(guild.getIdLong(), igsqBot).getPrefix();
				content = messageContent.substring(prefix.length()).trim();
			}
			else
			{
				return;
			}
		}
		else
		{
			prefix = Constants.DEFAULT_BOT_PREFIX;
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
			cmd = commandMap.get(commandText.toLowerCase());
			if(cmd == null)
			{
				event.getMessage().addReaction(Emoji.FAILURE.getAsReaction()).queue(success -> event.getMessage().removeReaction(Emoji.FAILURE.getAsReaction()).queueAfter(10, TimeUnit.SECONDS, null, error -> {}), error -> {});
				EmbedUtils.sendError(channel, "The command `" + commandText + "` was not found.\n Type `" + prefix + "help` for help.");
				return;
			}

			args.remove(0);
			if(startWithIdSpaced)
			{
				args.remove(0);
			}
			CommandContext ctx = new CommandContext(event, igsqBot, cmd, args);

			if(args.isEmpty())
			{
				cmd.process(ctx);
				return;
			}

			String subCommand = args.get(0);
			for(Command child : cmd.getChildren())
			{
				if(subCommand.equalsIgnoreCase(child.getName()))
				{
					args.remove(0);
					child.process(new CommandContext(event, igsqBot, child, args));
					return;
				}
			}
			cmd.process(ctx);
		}
	}
}
