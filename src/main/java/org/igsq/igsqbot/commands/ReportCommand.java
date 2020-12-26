package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.entities.yaml.GuildConfig;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.awt.*;
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
		final Guild guild = ctx.getGuild();
		final JDA jda = ctx.getJDA();

		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}

		final User reportedUser = UserUtils.getUserFromMention(args.get(0));
		if(reportedUser != null)
		{
			final Member reportedMember = UserUtils.getMemberFromUser(reportedUser, guild);
			final GuildConfig config = new GuildConfig(ctx.getGuild(), ctx.getJDA());
			final MessageChannel reportChannel = config.getReportChannel();
			args.remove(0);

			if(reportChannel == null)
			{
				new EmbedGenerator(channel).text("There is no report channel setup").color(Color.RED).sendTemporary();
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
				EmbedGenerator embed = new EmbedGenerator(reportChannel)
						.title("New report by: " + author.getAsTag())
						.element("Reporting user:", reportedMember.getAsMention())
						.element("Description:", ArrayUtils.arrayCompile(args, " "))
						.element("Channel:", StringUtils.getChannelAsMention(channel.getId()))
						.element("Message Log:", messageLog.toString())
						.color(reportedMember.getColor())
						.footer("This report is unhandled and can only be dealt by members higher than " + UserUtils.getMemberFromUser(author, guild).getRoles().get(0).getName());

				reportChannel.sendMessage(embed.getBuilder().build()).queue
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
		else EmbedUtils.sendError(channel, "Could not find the user " + args.get(0) + ".");
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
	public List<Permission> getPermissions()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isRequiresGuild()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 60;
	}
}
