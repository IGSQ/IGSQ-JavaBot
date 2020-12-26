package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Collections;
import java.util.List;

public class PrefixCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Guild guild = ctx.getGuild();
		final JDA jda = ctx.getJDA();
		final GuildConfig config = new GuildConfig(guild, jda);

		if(args.isEmpty())
		{
			new EmbedGenerator(channel)
					.text("The prefix for me is: " + (config.getGuildPrefix().equals(Constants.DEFAULT_BOT_PREFIX) ? "`.` the default." : "`" + config.getGuildPrefix() + "` custom set."))
					.color(Constants.IGSQ_PURPLE)
					.sendTemporary(30000);
		}
		else if(args.size() > 1 || args.get(0).length() > 5)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(args.get(0).equalsIgnoreCase("reset"))
		{
			EmbedUtils.sendSuccess(channel, "Reset my prefix to `" + Constants.DEFAULT_BOT_PREFIX + "`");
			config.setGuildPrefix(Constants.DEFAULT_BOT_PREFIX);
		}
		else
		{
			if(!ArrayUtils.isValueInArray(new String[]{" "}, args.get(0)))
			{
				config.setGuildPrefix(args.get(0));
				EmbedUtils.sendSuccess(channel, "My new prefix is `" + args.get(0) + "`");
			}
		}
	}

	@Override
	public String getName()
	{
		return "Prefix";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("prefix");
	}

	@Override
	public String getDescription()
	{
		return "Sets / gets the prefix for the bot in the current server.";
	}

	@Override
	public String getSyntax()
	{
		return "<newPrefix {5}> | <reset> | <none> ";
	}

	@Override
	public List<Permission> getPermissions()
	{
		return Collections.singletonList(Permission.ADMINISTRATOR);
	}

	@Override
	public boolean isRequiresGuild()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
