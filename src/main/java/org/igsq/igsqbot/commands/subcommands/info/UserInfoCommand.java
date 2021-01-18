package org.igsq.igsqbot.commands.subcommands.info;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.info.MemberInfo;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

public class UserInfoCommand extends Command
{
	public UserInfoCommand(Command parent)
	{
		super(parent, "user", "Shows information about a user", "[user]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		if(args.isEmpty())
		{
			showInfo(ctx.getMember(), ctx);
		}
		else
		{
			Guild guild = ctx.getGuild();
			new Parser(ArrayUtils.arrayCompile(args.subList(0, args.size()), " "), ctx).parseAsUser(
					user -> UserUtils.getMemberFromUser(user, guild).queue(
							member -> showInfo(member, ctx),
							error -> ctx.replyError("Member not found.")));
		}
	}

	private void showInfo(Member member, CommandContext ctx)
	{
		MemberInfo memberInfo = new MemberInfo(member);

		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("Information for user **" + memberInfo.getAsTag() + "**")
				.addField("Boosting since", memberInfo.getBoostingSince() == null ? "Not boosting" : StringUtils.parseDateTime(memberInfo.getBoostingSince()), true)
				.addField("Joined at", StringUtils.parseDateTime(memberInfo.getTimeJoined()), true)
				.addField("Created account at", StringUtils.parseDateTime(memberInfo.getTimeCreated()), true)
				.addField("Roles", memberInfo.getCondensedRoles().stream().map(Role::getAsMention).collect(Collectors.joining(" ")), true)
				.setThumbnail(memberInfo.getAvatarURL())
				.setColor(Constants.IGSQ_PURPLE)
				.setTimestamp(Instant.now())
				.build()).queue();
	}
}
