package org.igsq.igsqbot.commands;

import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.JsonBotConfig;
import org.igsq.igsqbot.entities.json.JsonGuildCache;
import org.igsq.igsqbot.entities.json.JsonPunishmentCache;
import org.igsq.igsqbot.entities.yaml.Blacklist;
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
		TaskHandler.close();
		MessageDataCache.close();
		Blacklist.close();

		ctx.replySuccess("IGSQBot going down NOW.");
		ctx.getJDA().shutdown();

		JsonGuildCache.getInstance().save();
		JsonPunishmentCache.getInstance().save();

		LOGGER.warn("IGSQBot was shutdown using shutdown command.");
		LOGGER.warn("-- Issued by: " + ctx.getAuthor().getAsTag());
		if(ctx.getGuild() != null)
		{
			LOGGER.warn("-- In guild: " + ctx.getGuild().getName());
		}
		else
		{
			LOGGER.warn("-- In guild: " + "Shutdown in DMs.");
		}
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
	public boolean canExecute(CommandContext ctx)
	{
		JsonBotConfig config = Json.get(JsonBotConfig.class, Filename.CONFIG);
		if(config == null)
		{
			return false;
		}
		else
		{
			return config.getPrivilegedUsers().contains(ctx.getAuthor().getId());
		}
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
