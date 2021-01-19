package org.igsq.igsqbot.commands.subcommands.channel;

import java.util.List;

import java.util.Optional;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;

public class ChannelIgnoreCommand extends Command
{
    public ChannelIgnoreCommand(Command parent)
    {
        super(parent, "ignore", "Controls ignored channels.", "[channel][true | false]");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
		CommandChecks.argsSizeSubceeds(ctx, 2);

		new Parser(args.get(0), ctx).parseAsTextChannel(
		channel ->
		{
			Optional<Boolean> bool = new Parser(args.get(1), ctx).parseAsBoolean();

			if(bool.isPresent())
			{
				if(bool.get())
				{
					if(BlacklistUtils.addChannel(channel, ctx.getGuild(), ctx.getIGSQBot()))
					{
						ctx.replySuccess("Blacklisted channel " + channel.getAsMention());
					}
					else
					{
						throw new CommandResultException("Channel " + channel.getAsMention() + " is already blacklisted.");
					}
				}
				else
				{
					if(BlacklistUtils.removeChannel(channel, ctx.getGuild(), ctx.getIGSQBot()))
					{
						ctx.replySuccess("Removed blacklist for channel " + channel.getAsMention());
					}
					else
					{
						throw new CommandResultException("Channel " + channel.getAsMention() + " is not blacklisted.");
					}
				}
			}
		});
    }
}
