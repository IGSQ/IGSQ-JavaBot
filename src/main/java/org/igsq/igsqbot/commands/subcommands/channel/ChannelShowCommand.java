package org.igsq.igsqbot.commands.subcommands.channel;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
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
    public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		StringBuilder text = new StringBuilder();
		for(ChannelBlacklists channel : BlacklistUtils.getBlacklistedChannels(cmd.getGuild(), cmd.getIGSQBot()))
		{
			text.append(StringUtils.getChannelAsMention(channel.getChannelId())).append(" is blacklisted.");
		}

		cmd.sendMessage(new EmbedBuilder()
				.setTitle("Configured channels for " + cmd.getGuild().getName())
				.addField("Blacklisted Channels", text.length() == 0 ? "No blacklisted channels" : text.toString(), false));
    }
}