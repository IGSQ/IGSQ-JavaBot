package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MuteCommand extends Command
{
	public MuteCommand()
	{
		super("Mute", new String[]{"mute"}, "Mutes the specified user.", "[user][duration]", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}
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
			LocalDateTime muteTime = CommandUtils.parseTime(args.get(1));

			if(muteTime == null)
			{
				EmbedUtils.sendError(channel, "Invalid time entered, specify the time as: EXAMPLE HERE");
			}
			else
			{
				EmbedUtils.sendSuccess(channel, "Member " + member.getAsMention() + " muted until " + muteTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			}
		}
	}
}
