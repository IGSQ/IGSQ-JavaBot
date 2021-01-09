package org.igsq.igsqbot.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Mute;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MuteCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			new Parser(args.get(0), ctx).parseAsUser(user ->
			{
				LocalDateTime muteTime = new Parser(args.get(1), ctx).parseAsDuration();
				MessageChannel channel = ctx.getChannel();
				User author = ctx.getAuthor();
				User selfUser = ctx.getIGSQBot().getSelfUser();
				Guild guild = ctx.getGuild();
				Role mutedRole = guild.getRoleById(new GuildConfig(guild.getIdLong(), ctx.getIGSQBot()).getMutedRole());

				if(mutedRole == null)
				{
					ctx.replyError("Muted role is not setup, cannot continue.");
					return;
				}

				CommandUtils.interactionCheck(selfUser, user, ctx, () ->
				{
					CommandUtils.interactionCheck(author, user, ctx, () ->
					{
						UserUtils.getMemberFromUser(user, guild).queue(member ->
						{
							List<Long> roleIds = UserUtils.getRoleIds(member);
							guild.modifyMemberRoles(member, mutedRole).queue(
									success ->
									{
										new Mute(member.getIdLong(), roleIds, guild, muteTime, ctx.getIGSQBot()).add();
										ctx.replySuccess("Muted " + user.getAsMention() + " until " + StringUtils.parseDateTime(muteTime));
									},
									error ->
									{
										ctx.replyError("An error occurred during mute. Check members roles");
										new ErrorHandler(new Exception(error));
									}
							);
						});
					});
				});
			});
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
		return Arrays.asList("mute", "tempban");
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
