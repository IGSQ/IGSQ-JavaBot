package org.igsq.igsqbot.commands.subcommands.channel;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.jooq.tables.pojos.ChannelBlacklists;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.StringUtils;

public class ChannelShowCommand extends Command
{
    public ChannelShowCommand(Command parent)
    {
        super(parent, "show", "Shows all configured channels for this server.", "[none]");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
	{
		StringBuilder text = new StringBuilder();
		for(ChannelBlacklists channel : BlacklistUtils.getBlacklistedChannels(ctx.getGuild(), ctx.getIGSQBot()))
		{
			text.append(StringUtils.getChannelAsMention(channel.getChannelId())).append(" is blacklisted.");
		}

		ctx.sendMessage(new EmbedBuilder()
				.setTitle("Configured channels for " + ctx.getGuild().getName())
				.addField("Blacklisted Channels", text.length() == 0 ? "No blacklisted channels" : text.toString(), false));
    }
}