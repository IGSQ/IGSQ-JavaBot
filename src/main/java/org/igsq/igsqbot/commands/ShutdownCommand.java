package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.GUIGenerator;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.entities.yaml.Blacklist;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.util.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class ShutdownCommand extends Command
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownCommand.class);

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx.getChannel(), this);
			return;
		}

		CommandHandler.close();
		GUIGenerator.close();
		TaskHandler.close();
		MessageDataCache.close();
		Blacklist.close();

		ctx.getJDA().shutdown();

		Yaml.saveFileChanges(Filename.ALL);
		Yaml.disregardAndCloseFile(Filename.ALL);

		LOGGER.warn("\nIGSQBot was shutdown using shutdown command.\n    " +
				"-- Issued by: " + ctx.getAuthor().getAsTag() + "\n    " +
				"-- In guild: " + ctx.getGuild().getName());
		System.exit(0);
	}

	@Override
	public String getName()
	{
		return "Shutdown";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("shutdown");
	}

	@Override
	public String getDescription()
	{
		return "Shuts the bot down using the proper methods";
	}

	@Override
	public String getSyntax()
	{
		return "[none]";
	}

	@Override
	public List<Permission> getPermissions()
	{
		return null;
	}

	@Override
	public boolean isRequiresGuild()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
