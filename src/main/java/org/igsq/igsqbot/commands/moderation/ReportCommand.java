package org.igsq.igsqbot.commands.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.cache.GuildConfigCache;
import org.igsq.igsqbot.entities.cache.PunishmentCache;
import org.igsq.igsqbot.entities.json.GuildConfig;
import org.igsq.igsqbot.entities.json.Punishment;
import org.igsq.igsqbot.entities.json.Report;
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
		final StringBuilder messageLog = new StringBuilder();
		final User author = ctx.getAuthor();

		if(args.size() < 2 || ctx.getMessage().getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}

		final Member reportedMember = ctx.getMessage().getMentionedMembers().get(0);
		final User reportedUser = reportedMember.getUser();
		GuildConfig config = GuildConfigCache.getInstance().get(ctx.getGuild().getId());
		args.remove(0);

		if(config.getReportChannel() == null)
		{
			ctx.replyError("There is no report channel setup");
		}
//		else if(reportedUser.equals(author))
//		{
//			ctx.replyError("You can't report yourself!");
//		}
//		else if(reportedUser.isBot())
//		{
//			ctx.replyError("You may not report bots.");
//		}
//		else if(reportedMember.isOwner())
//		{
//			ctx.replyError("You may not report the owner.");
//		}
		else
		{
			final MessageChannel reportChannel = ctx.getGuild().getTextChannelById(config.getReportChannel());
			if(reportChannel != null)
			{
				for(Message selectedMessage : channel.getHistory().retrievePast(5).complete())
				{
					if(selectedMessage.getAuthor().getId().equals(reportedMember.getId()))
					{
						messageLog.append(reportedMember.getAsMention()).append(" | ").append(selectedMessage.getContentRaw()).append("\n");
					}
				}

				if(messageLog.length() == 0) messageLog.append("No recent messages found for this user.");

				reportChannel.sendMessage(new EmbedBuilder()
						.setTitle("New report by: " + author.getAsTag())
						.addField("Reporting user:", reportedMember.getAsMention(), false)
						.addField("Description:", ArrayUtils.arrayCompile(args, " "), false)
						.addField("Channel:", StringUtils.getChannelAsMention(channel.getId()), false)
						.addField("Message Log:", messageLog.toString(), false)
						.setColor(reportedMember.getColor())
						.setFooter("This report is unhandled and can only be dealt by members higher than " + reportedMember.getRoles().get(0).getName())
						.build()).queue
						(
								message ->
								{
									Punishment punishment = PunishmentCache.getInstance().get(ctx.getGuild().getId(), reportedUser.getId());
									Report report = new Report(message.getId(), ctx.getGuild().getId(), author.getId(), reportedUser.getId());
									List<Report> reports = punishment.getReports();
									reports.add(report);
									punishment.setReports(reports);
									message.addReaction(Constants.THUMB_UP).queue();
								}
						);
			}
		}
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
