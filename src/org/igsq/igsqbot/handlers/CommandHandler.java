package org.igsq.igsqbot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		final String content = event.getMessage().getContentRaw().substring(Common.BOT_PREFIX.length());
		final Message message = event.getMessage();
		final String issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
		final Command cmd = COMMANDS.get(issuedCommand);
		final MessageChannel channel = event.getChannel();
		final List<String> args = Arrays.asList(event.getMessage().getContentRaw().split(" "));
		args.remove(0);

		if(!message.getContentRaw().startsWith(Common.BOT_PREFIX) || event.getAuthor().isBot())
		{
			return;
		}

		if(cmd == null)
		{
			EmbedUtils.sendError(channel, "Command `" + issuedCommand + "` was not found");
			return;
		}

		if(cmd.isRequiresGuild() && (!event.getChannelType().equals(ChannelType.TEXT)||!event.getMember().hasPermission(cmd.getRequiredPermissions())))
		{
			EmbedUtils.sendError(channel, "This command cannot be executed here, either it requires execution in a server, or a permission error occurred.");
		}

		if(event.getChannelType().equals(ChannelType.TEXT) && event.getGuild().getSelfMember().hasPermission((GuildChannel) channel, Permission.MESSAGE_MANAGE))
		{
			event.getMessage().delete().queue();
		}

		commandExecutor.submit(() -> cmd.execute(args, new Context(event)));
	}
}
