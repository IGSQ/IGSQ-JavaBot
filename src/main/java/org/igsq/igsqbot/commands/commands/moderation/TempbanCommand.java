package org.igsq.igsqbot.commands.commands.moderation;

import java.time.LocalDateTime;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Tempban;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.*;

@SuppressWarnings("unused")
public class TempbanCommand extends Command
{
	public TempbanCommand()
	{
		super("Tempban", "Temporarily bans a user.", "[user][duration]");
		addAliases("tempban", "mute");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addSelfPermissions(Permission.MANAGE_ROLES);
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsSizeMatches(ctx, 2);

		new Parser(args.get(0), ctx).parseAsUser(user ->
		{
			LocalDateTime muteTime = new Parser(args.get(1), ctx).parseAsDuration();
			User author = ctx.getAuthor();
			User selfUser = ctx.getIGSQBot().getSelfUser();
			Guild guild = ctx.getGuild();
			Role tempBanRole = guild.getRoleById(new GuildConfig(guild.getIdLong(), ctx.getIGSQBot()).getTempBanRole());

			CommandChecks.roleConfigured(tempBanRole, "Tempban role");

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
									new Tempban(member.getIdLong(), roleIds, guild, muteTime, ctx.getIGSQBot()).add();
									ctx.replySuccess("Tempbanned " + user.getAsMention() + " until " + StringUtils.parseDateTime(muteTime));
								},
								error ->
								{
									ctx.replyError("An error occurred during tempban. Check members roles");
									new ErrorHandler(new Exception(error));
								}
						);
					});
				});
			});
		});

	}
}
