package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.json.JsonPunishment;
import org.igsq.igsqbot.entities.json.JsonPunishmentCache;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class MuteCommand extends Command
{
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
			JsonPunishment jsonPunishment = (JsonPunishment) JsonPunishmentCache.getInstance().get(member);

			if(jsonPunishment.isMuted())
			{
				EmbedUtils.sendError(channel, "User: " + member.getAsMention() + " is already muted!");
			}
			else if(muteTime == null)
			{
				EmbedUtils.sendError(channel, "Invalid time entered. Send time as: \n1d - 1day\n1h - 1 hour\n30m - 30 minutes\n Only 1 option allowed. ");
			}
			else
			{
				jsonPunishment.setRoles(UserUtils.getRoleIds(member));
				jsonPunishment.setMutedUntil(muteTime.atZone(CommandUtils.getLocalOffset()).toInstant().toEpochMilli());
				jsonPunishment.setMuted(true);
				EmbedUtils.sendSuccess(channel, "Muted member: " + member.getAsMention() + " until " + muteTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
			}

		}
	}

	@Override
	public String getName()
	{
		return "Mute";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("mute");
	}

	@Override
	public String getDescription()
	{
		return "Mutes the specified user.";
	}

	@Override
	public String getSyntax()
	{
		return "[user][duration]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.hasPermission(Collections.singletonList(Permission.KICK_MEMBERS));
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 60;
	}
}
