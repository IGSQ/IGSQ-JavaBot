package org.igsq.igsqbot.commands.commands.moderation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Tempban;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.*;

@SuppressWarnings("unused")
public class TempbanCommand extends Command
{
	public TempbanCommand()
	{
		super("Tempban", "Temporarily bans a user.", "[user][duration] / [remove]");
		addAliases("tempban", "mute");
		addChildren(new TempbanRemoveCommand(this));
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addSelfPermissions(Permission.MANAGE_ROLES);
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeMatches(ctx, 2, failure)) return;

		new Parser(args.get(0), ctx).parseAsUser(user ->
		{
			LocalDateTime muteTime = new Parser(args.get(1), ctx).parseAsDuration();
			User author = ctx.getAuthor();
			User selfUser = ctx.getIGSQBot().getSelfUser();
			Guild guild = ctx.getGuild();
			Role tempBanRole = guild.getRoleById(new GuildConfig(guild.getIdLong(), ctx.getIGSQBot()).getTempBanRole());

			if(CommandChecks.roleConfigured(tempBanRole, "Tempban role", failure)) return;


			if(muteTime == null || muteTime.isAfter(LocalDateTime.now().plusWeeks(1)))
			{
				failure.accept(new CommandInputException("Duration " + args.get(1) + " is invalid."));
				return;
			}

			CommandUtils.interactionCheck(selfUser, user, ctx, () ->
			{
				CommandUtils.interactionCheck(author, user, ctx, () ->
				{
					UserUtils.getMemberFromUser(user, guild).queue(member ->
					{
						List<Long> roleIds = UserUtils.getRoleIds(member);
						guild.modifyMemberRoles(member, tempBanRole).queue(
								success ->
								{
									if(Tempban.add(member.getIdLong(), roleIds, guild, muteTime, ctx.getIGSQBot()))
									{
										ctx.replySuccess("Tempbanned " + user.getAsMention() + " until " + StringUtils.parseDateTime(muteTime));
									}
									else
									{
										failure.accept(new CommandResultException("User " + user.getAsMention() + " is already tempbanned."));
									}
								}
						);
					});
				});
			});
		});
	}

	public static class TempbanRemoveCommand extends Command
	{
	    public TempbanRemoveCommand(Command parent)
	    {
	        super(parent, "remove", "Removes a tempban", "[user]");
	    }

	    @Override
	    public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	    {
			if(CommandChecks.argsEmpty(ctx, failure)) return;

			new Parser(args.get(0), ctx).parseAsUser(user ->
			{
				User author = ctx.getAuthor();
				User selfUser = ctx.getIGSQBot().getSelfUser();
				Guild guild = ctx.getGuild();

				CommandUtils.interactionCheck(selfUser, user, ctx, () ->
				{
					CommandUtils.interactionCheck(author, user, ctx, () ->
					{
						UserUtils.getMemberFromUser(user, guild).queue(member ->
						{
							Tempban.remove(member.getIdLong(), ctx.getIGSQBot());
							ctx.replySuccess("Removed tempban for user " + UserUtils.getAsMention(member.getIdLong()));
						});
					});
				});
			});
	    }
	}
}
