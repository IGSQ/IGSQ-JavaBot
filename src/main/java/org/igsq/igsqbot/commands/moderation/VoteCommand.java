package org.igsq.igsqbot.commands.moderation;

import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class VoteCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(args.size() < 4)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			Guild guild = ctx.getGuild();
			List<String> options = new Parser(args.get(0), ctx).parseAsSlashArgs();
			List<String> roleArgs = new Parser(args.get(1), ctx).parseAsSlashArgs();
			LocalDateTime timestamp = new Parser(args.get(2), ctx).parseAsDuration();
			String subject = String.join(" ", args.subList(3, args.size()));
			MessageChannel voteChannel = guild.getTextChannelById(new GuildConfig(guild, ctx.getIGSQBot()).getVoteChannel());

			Message message = ctx.getMessage();

			List<Role> roles;

			if(message.getMentionedRoles().size() == roleArgs.size())
			{
				roles = message.getMentionedRoles();
			}
			else
			{
				roles = roleArgs.stream().map(guild::getRoleById).filter(Objects::nonNull).collect(Collectors.toList());
			}

			if(roles.isEmpty())
			{
				EmbedUtils.sendSyntaxError(ctx);
				return;
			}

			if(voteChannel == null)
			{
				ctx.replyError("Vote channel not setup.");
			}
			else
			{
				guild.findMembers(member ->
				{
					for(Role role : roles)
					{
						if(member.getRoles().contains(role))
						{
							return true;
						}
					}
					return false;
				}).onSuccess(members ->
				{
					List<Long> id = members.stream().map(Member::getIdLong).collect(Collectors.toList());
				});
			}
		}
	}

	@Override
	public String getName()
	{
		return "Voting";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("vote");
	}

	@Override
	public String getDescription()
	{
		return "Starts a vote.";
	}

	@Override
	public String getSyntax()
	{
		return "[options] [roles] [timestamp] [subject]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return false;
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public long getCooldown()
	{
		return 0;
	}
}
