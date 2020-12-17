package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.*;

import java.awt.*;
import java.util.List;

public class Report_Command extends Command
{
	public Report_Command()
	{
		super("Report", new String[]{"report"}, "Reports the specified member with the specified reason","[user][reason]",new Permission[]{}, true, 60);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final StringBuilder messageLog = new StringBuilder();
		final User author = ctx.getAuthor();
		final JDA jda = ctx.getJDA();
		final Guild guild = ctx.getGuild();

		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(channel,this);
			return;
		}

		final User reportedUser = UserUtils.getUserFromMention(args.get(0));
		if(reportedUser != null)
		{
			Member reportedMember = UserUtils.getMemberFromUser(reportedUser, guild);
			MessageChannel reportChannel = jda.getTextChannelById(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild"));
			args.remove(0);

			if(reportChannel == null)
			{
				new EmbedGenerator(channel).text("There is no report channel setup").color(Color.RED).sendTemporary();
				return;
			}

			if(reportedUser.equals(author)) EmbedUtils.sendError(channel, "You can't report yourself!");
			else if(reportedUser.isBot()) EmbedUtils.sendError(channel, "You may not report bots.");
			else if(reportedMember.isOwner()) EmbedUtils.sendError(channel, "You may not report the owner.");
			else if(YamlUtils.isFieldEmpty(guild.getId() + ".reportchannel", "guild")) EmbedUtils.sendError(channel, "There is no report channel setup.");
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
									Yaml.updateField(message.getId() + ".report.reporteduser", "internal", reportedMember.getId());
									Yaml.updateField(message.getId() + ".report.reportinguser", "internal", author.getId());
									Yaml.updateField(message.getId() + ".report.enabled", "internal", true);
									message.addReaction("U+1F44D").queue();
								}
						);

			}
		}
		else EmbedUtils.sendError(channel, "Could not find the user " + args.get(0) + ".");
	}
}
