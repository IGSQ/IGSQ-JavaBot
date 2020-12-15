package org.igsq.igsqbot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public abstract class CommandHandler
{
	private CommandHandler()
	{
		//Overrides the default, public, constructor
	}
	public static final String COMMAND_PACKAGE = "org.igsq.igsqbot.commands";
	private static final Map<String, Command> COMMANDS = new HashMap<>();
	private static final ClassGraph CLASS_GRAPH = new ClassGraph().acceptPackages(COMMAND_PACKAGE);
	private static final ExecutorService commandExecutor = Executors.newFixedThreadPool(5);

	static
	{
		try (final ScanResult result = CLASS_GRAPH.scan())
		{
			for (final ClassInfo cls : result.getAllClasses())
			{
				final Command cmd = (Command) cls.loadClass().getDeclaredConstructor().newInstance();
				COMMANDS.put(cmd.getInvoke(), cmd);
				for (final String alias : cmd.getAliases()) COMMANDS.put(alias, cmd);
			}
		}
		catch (Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static final Map<String, Command> COMMAND_MAP = Collections.unmodifiableMap(COMMANDS);

	public static void handle(MessageReceivedEvent event)
	{
		final List<String> args = Arrays.stream(event.getMessage().getContentRaw().split(" ")).collect(Collectors.toList());
		final ChannelType channelType = event.getChannelType();
		final MessageChannel channel = event.getChannel();
		args.remove(0);

		if(channelType.equals(ChannelType.TEXT))
		{
			final Guild guild = event.getGuild();
			final String prefix = YamlUtils.getGuildPrefix(guild.getId());
			final Member member = event.getMember();
			final String selfID = IGSQBot.getJDA().getSelfUser().getId();
			final String messageContent = event.getMessage().getContentRaw();

			final String content;
			final String issuedCommand;
			final Command cmd;
			if(messageContent.startsWith(prefix))
			{
				content = messageContent.substring(prefix.length()).trim();
				issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
				cmd = COMMANDS.get(issuedCommand);
			}
			else if(messageContent.startsWith("<@" + selfID + ">") || messageContent.startsWith("<@!" + selfID + ">"))
			{
				content = messageContent.substring(messageContent.indexOf(">") + 1).trim();
				issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
				cmd = COMMANDS.get(issuedCommand);
			}
			else
			{
				return;
			}

			if(cmd == null)
			{
				EmbedUtils.sendError(channel, "The command `" + issuedCommand + "` was not found.");
				return;
			}
			if(!guild.getSelfMember().hasPermission((GuildChannel) channel, cmd.getRequiredPermissions())
					|| !member.hasPermission((GuildChannel) channel, cmd.getRequiredPermissions()))
			{
				EmbedUtils.sendPermissionError(channel, cmd);
				return;
			}
			if(guild.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
			{
				event.getMessage().delete().queue();
			}
			commandExecutor.submit(() -> cmd.execute(args, new Context(event)));

		}
		else
		{
			final String prefix = Common.DEFAULT_BOT_PREFIX;
			if(event.getMessage().getContentRaw().startsWith(prefix))
			{
				final String content = event.getMessage().getContentRaw().substring(prefix.length());
				final String issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
				final Command cmd = COMMANDS.get(issuedCommand);

				if(cmd.isRequiresGuild())
				{
					EmbedUtils.sendError(channel, "This command requires execution in a guild.");
				}
				else
				{
					commandExecutor.submit(() -> cmd.execute(args, new Context(event)));
				}
			}

		}
	}
}
