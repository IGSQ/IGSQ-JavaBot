package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.GUIGenerator;
import org.igsq.igsqbot.objects.cache.MessageDataCache;
import org.igsq.igsqbot.util.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShutdownCommand extends Command
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownCommand.class);

	public ShutdownCommand()
	{
		super("Shutdown", new String[]{"shutdown"}, "Shuts the bot down using the proper methods", "[none]", new Permission[]{Permission.ADMINISTRATOR}, false, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx.getChannel(), this);
			return;
		}

		ctx.getJDA().shutdown();
		CommandHandler.shutdown();
		GUIGenerator.closeAll();
		TaskHandler.close();
		MessageDataCache.close();

		Yaml.saveFileChanges("@all");
		Yaml.disregardAndCloseFile("@all");

		LOGGER.warn("\nIGSQBot shutdown using shutdown command.\n    " +
				"-- Issued by: " + ctx.getAuthor().getAsTag() + "\n    " +
				"-- In guild: " + ctx.getGuild().getName());
		System.exit(0);
	}
}
