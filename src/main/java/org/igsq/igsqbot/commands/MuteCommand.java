package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.json.GuildConfig;
import org.igsq.igsqbot.entities.cache.GuildConfigCache;
import org.igsq.igsqbot.entities.json.Punishment;
import org.igsq.igsqbot.entities.cache.PunishmentCache;
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
		MessageChannel channel = ctx.getChannel();

		if(args.size() != 2 || ctx.getMessage().getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			Member member = ctx.getMessage().getMentionedMembers().get(0);
			LocalDateTime muteTime = CommandUtils.parseTime(args.get(1));
			Punishment punishment = PunishmentCache.getInstance().get(member);
			Guild guild = ctx.getGuild();
			GuildConfig guildConfig = GuildConfigCache.getInstance().get(guild.getId());
			Role mutedRole = guild.getRoleById(guildConfig.getMutedRole());

			if(mutedRole == null)
			{
				ctx.replyError("Muted role not found, muting cannot proceed.");
			}
			else if(!guild.getSelfMember().canInteract(member) || !ctx.getMember().canInteract(member))
			{
				EmbedUtils.sendPermissionError(channel, this);
			}
			else if(punishment.isMuted())
			{
				ctx.replyError("User: " + member.getAsMention() + " is already muted!");
			}
			else if(muteTime == null)
			{
				ctx.replyError("Invalid time entered. Send time as: \n1d - 1day\n1h - 1 hour\n30m - 30 minutes\n Only 1 option allowed.");
			}
			else
			{
				punishment.setRoles(UserUtils.getRoleIds(member));
				punishment.setMutedUntil(muteTime.atZone(CommandUtils.getLocalOffset()).toInstant().toEpochMilli());
				punishment.setMuted(true);

				guild.modifyMemberRoles(member, mutedRole).queue(success -> ctx.replySuccess("Muted member: " + member.getAsMention() + " until " + muteTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
				,error ->
						{
							ctx.replyError("An error occurred during role removal, muting halted.");
							punishment.setMuted(false);
							punishment.setRoles(Collections.emptyList());
							punishment.setMutedUntil(-1);
						});
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
		return ctx.hasPermission(Collections.singletonList(Permission.MESSAGE_MANAGE));
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
