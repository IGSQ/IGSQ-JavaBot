package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.info.MemberInfo;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.UserUtils;

public class UserInfoCommand extends Command
{
	public UserInfoCommand(Command parent)
	{
		super(parent, "user", "Shows information about a user", "[user]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			Guild guild = ctx.getGuild();
			new Parser(ArrayUtils.arrayCompile(args.subList(0, args.size()), " "), ctx).parseAsUser(user ->
					UserUtils.getMemberFromUser(user, guild).queue(member ->
							{
								MemberInfo memberInfo = new MemberInfo(member);

								ctx.getChannel().sendMessage(new EmbedBuilder()
										.setTitle("Info for " + memberInfo.getNickname())
										.setDescription("")
										.build()).queue();
							},
							error -> ctx.replyError("Member not found.")));
		}
	}
}
