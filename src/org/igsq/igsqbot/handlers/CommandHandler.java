package org.igsq.igsqbot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.ErrorHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler
{
	private CommandHandler()
	{
		//Overrides the default, public, constructor
	}

	private static final ClassGraph CLASS_GRAPH = new ClassGraph().acceptPackages("org.igsq.igsqbot.improvedcommands");
	private static final Map<String, Command> COMMANDS = new HashMap<>();

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
		catch (final Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static void handle(final MessageReceivedEvent event)
	{
		final String content = event.getMessage().getContentRaw().substring(Common.BOT_PREFIX.length());
		final Message message = event.getMessage();

		if(!message.getContentRaw().startsWith(Common.BOT_PREFIX) || event.getAuthor().isBot())
		{
			return;
		}

		final String issuedCommand = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
		final Command cmd = COMMANDS.get(issuedCommand);
		final MessageChannel channel = event.getChannel();
		final String[] args = Common.depend(event.getMessage().getContentRaw().split(" "), 0);

		if(cmd == null)
		{
			new EmbedGenerator(channel).text("Command " + issuedCommand + " was not found")
			.color(Color.RED)
			.sendTemporary();
			return;
		}

		cmd.execute(args, new Context(event));
	}
}
