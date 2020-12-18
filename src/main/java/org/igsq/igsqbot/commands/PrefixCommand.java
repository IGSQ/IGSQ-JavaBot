package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.GuildConfig;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;

public class PrefixCommand extends Command
{
	public PrefixCommand()
	{
		super("Prefix", new String[]{"prefix"}, "Sets / gets the prefix for the bot in the current server.", "[newPrefix -5-] | [reset] | [none] ", new Permission[]{}, true, 0);
	}

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
					.text("The prefix for me is: " + (config.getGuildPrefix().equals(Common.DEFAULT_BOT_PREFIX) ? "`.` the default." : "`" + config.getGuildPrefix() + "` custom set."))
					.color(EmbedUtils.IGSQ_PURPLE)
					.sendTemporary(30000);
		}
		else if(args.size() > 1 || args.get(0).length() > 5)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(args.get(0).equalsIgnoreCase("reset"))
		{
			EmbedUtils.sendSuccess(channel, "Reset my prefix to `.`");
			config.setGuildPrefix(".");
		}
		else
		{
			config.setGuildPrefix(args.get(0));
			EmbedUtils.sendSuccess(channel, "My new prefix is `" + args.get(0) + "`");
		}
	}
}
