package org.igsq.igsqbot.commands;

import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.entities.json.JsonGuildCache;
import org.igsq.igsqbot.entities.json.JsonPunishmentCache;
import org.igsq.igsqbot.entities.yaml.Blacklist;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Collections;
import java.util.List;

public class ShutdownCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx.getChannel(), this);
			return;
		}

		ctx.getIGSQBot().getCommandHandler().close();
		ctx.getIGSQBot().getTaskHandler().close();
		ctx.getIGSQBot().getMinecraft().close();
		MessageDataCache.close();
		Blacklist.close();

		ctx.replySuccess("IGSQBot going down NOW.");
		ctx.getJDA().shutdown();

		JsonGuildCache.getInstance().save();
		JsonPunishmentCache.getInstance().save();

		ctx.getIGSQBot().getLogger().warn("-- IGSQBot was shutdown using shutdown command.");
		ctx.getIGSQBot().getLogger().warn("-- Issued by: " + ctx.getAuthor().getAsTag());
		if(ctx.getGuild() != null)
		{
			ctx.getIGSQBot().getLogger().warn("-- In guild: " + ctx.getGuild().getName());
		}
		else
		{
			ctx.getIGSQBot().getLogger().warn("-- In guild: " + "Shutdown in DMs.");
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
		return ctx.isDeveloper();
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
