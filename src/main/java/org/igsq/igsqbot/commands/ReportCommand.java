package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReportCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final StringBuilder messageLog = new StringBuilder();
		final User author = ctx.getAuthor();
		final JDA jda = ctx.getJDA();

		if(args.size() != 2 || ctx.getMessage().getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}

			final Member reportedMember = ctx.getMessage().getMentionedMembers().get(0);
			final User reportedUser = reportedMember.getUser();
			final GuildConfig config = new GuildConfig(ctx.getGuild(), ctx.getJDA());
			final MessageChannel reportChannel = config.getReportChannel();
			args.remove(0);

			if(reportChannel == null)
			{
				EmbedUtils.sendError(channel, "There is no report channel setup");
				return;
			}

			if(reportedUser.equals(author)) EmbedUtils.sendError(channel, "You can't report yourself!");
			else if(reportedUser.isBot()) EmbedUtils.sendError(channel, "You may not report bots.");
			else if(reportedMember.isOwner()) EmbedUtils.sendError(channel, "You may not report the owner.");
			else
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
									final MessageDataCache messageDataCache = new MessageDataCache(message.getId(), jda);
									final Map<String, String> users = new ConcurrentHashMap<>();

									users.put("reporteduser", reportedMember.getId());
									users.put("reportinguser", author.getId());
									messageDataCache.setType(MessageDataCache.MessageType.REPORT);
									messageDataCache.setUsers(users);
									messageDataCache.build();

									message.addReaction("U+1F44D").queue();
								}
						);
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
