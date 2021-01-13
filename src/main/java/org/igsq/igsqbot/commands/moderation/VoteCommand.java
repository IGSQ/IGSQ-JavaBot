package org.igsq.igsqbot.commands.moderation;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Vote;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

import java.time.LocalDateTime;
import java.util.*;
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
			List<Role> roles = new Parser(args.get(1), ctx).parseAsSlashArgs().stream()
					.map(guild::getRoleById)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

			List<Long> memberIds = new ArrayList<>();
			roles.stream()
					.map(role -> guild.getMembersWithRoles(roles))
					.forEach(memberList -> memberList.forEach(member -> memberIds.add(member.getIdLong())));

			LocalDateTime timestamp = new Parser(args.get(2), ctx).parseAsDuration();
			String subject = String.join(" ", (String[]) Arrays.copyOfRange(args.toArray(), 3, args.size()));

			MessageChannel voteChannel = guild.getTextChannelById(new GuildConfig(guild, ctx.getIGSQBot()).getVoteChannel());

			if(voteChannel == null)
			{
				ctx.replyError("Vote channel not setup.");
			}
			else
			{
				Vote vote = new Vote(options, memberIds, timestamp, subject, voteChannel, ctx.getIGSQBot());
				vote.send();
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
		return true;
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
