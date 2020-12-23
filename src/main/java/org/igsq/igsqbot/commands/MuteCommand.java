package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MuteCommand extends Command
{
	public MuteCommand()
	{
		super("Mute", new String[]{"mute"}, "Mutes the specified user.", "[user][duration]", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(MuteCommand.class);
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		if(args.size() != 2 || ctx.getMessage().getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			Member member = ctx.getMessage().getMentionedMembers().get(0);
			LocalTime time = CommandUtils.parseTime(args.get(1));
			LocalTime now = LocalTime.now();

			if(time == null)
			{
				EmbedUtils.sendError(channel, "Invalid time entered.");
			}
			else
			{
				Duration duration = Duration.between(now, time);

				if(duration.isNegative())
				{
					EmbedUtils.sendError(channel, "Enter a time in the future.");
				}
				if(duration.toHours() > 20)
				{
					EmbedUtils.sendError(channel, "Maximum duration exceeded.");
				}
			}


		}
	}
}
