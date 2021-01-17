package org.igsq.igsqbot.commands.commands.moderation;

import java.time.LocalDateTime;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Tempban;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.*;

public class TempbanCommand extends Command
{
	public TempbanCommand()
	{
		super("Tempban", "Temporarily bans a user.", "[user][duration]");
		addAliases("tempban", "mute");
		addPermissions(Permission.KICK_MEMBERS);
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
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
				User author = ctx.getAuthor();
				User selfUser = ctx.getIGSQBot().getSelfUser();
				Guild guild = ctx.getGuild();
				Role mutedRole = guild.getRoleById(new GuildConfig(guild.getIdLong(), ctx.getIGSQBot()).getMutedRole());

				if(mutedRole == null)
				{
					ctx.replyError("Tempban role is not setup, cannot continue.");
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
}
