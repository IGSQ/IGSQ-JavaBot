package org.igsq.igsqbot.commands.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;

import java.util.Collections;
import java.util.List;

public class ReportCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();

		if(args.size() < 2 || ctx.getMessage().getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}

		final Member reportedMember = ctx.getMessage().getMentionedMembers().get(0);
		final User reportedUser = reportedMember.getUser();
		args.remove(0);

		if(reportedUser.equals(author))
		{
			ctx.replyError("You can't report yourself!");
		}
		else if(reportedUser.isBot())
		{
			ctx.replyError("You may not report bots.");
		}
		else if(reportedMember.isOwner())
		{
			ctx.replyError("You may not report the owner.");
		}

		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("New report by: " + author.getAsTag())
				.addField("Reporting user:", reportedMember.getAsMention(), false)
				.addField("Description:", ArrayUtils.arrayCompile(args, " "), false)
				.addField("Channel:", StringUtils.getChannelAsMention(channel.getId()), false)
				.addField("Message Link:", "", false)
				.setColor(reportedMember.getColor())
				.setFooter("This report is unhandled and can only be dealt by members higher than " + reportedMember.getRoles().get(0).getName())
				.build()).queue
				(
						message ->
						{
							message.addReaction(Constants.THUMB_UP).queue();
						}
				);

	}

	@Override
	public String getName()
	{
		return "Report";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("report");
	}

	@Override
	public String getDescription()
	{
		return "Reports the specified member with the specified reason";
	}

	@Override
	public String getSyntax()
	{
		return "[user] [reason]";
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
	public int getCooldown()
	{
		return 60;
	}
}
