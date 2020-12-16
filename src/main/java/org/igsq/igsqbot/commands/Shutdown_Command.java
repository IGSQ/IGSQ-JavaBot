package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.util.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Shutdown_Command extends Command
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Shutdown_Command.class);
	public Shutdown_Command()
	{
		super("shutdown", new String[]{}, "Shuts the bot down using the proper methods","[none]", new Permission[]{Permission.ADMINISTRATOR}, false, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx.getChannel(),this);
			return;
		}

		ctx.getJDA().shutdown();
		Common.scheduler.shutdown();
		CommandHandler.shutdown();

		Yaml.saveFileChanges("@all");
		Yaml.disregardAndCloseFile("@all");

		LOGGER.warn("\nIGSQBot shutdown using shutdown command.\n    " +
				"-- Issued by: " + ctx.getAuthor().getAsTag() + "\n    " +
				"-- In guild: " + ctx.getGuild().getName());
		System.exit(0);
	}
}
