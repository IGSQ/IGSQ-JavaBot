package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.Arrays;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.*;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Report_Command extends Command
{
	public Report_Command()
	{
		super("report", new String[]{}, "Reports the specified member with the specified reason", new Permission[]{}, true, 60);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final StringBuilder messageLog = new StringBuilder();
		final User author = ctx.getAuthor();
		final JDA jda = ctx.getJDA();
		final Guild guild = ctx.getGuild();
		final StringBuilder reportDescription = new StringBuilder();

		final User reportedUser = User_Utils.getUserFromMention(args[0]);
		if(reportedUser != null)
		{
			Member reportedMember = User_Utils.getMemberFromUser(reportedUser, guild);
			MessageChannel reportChannel = jda.getTextChannelById(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild"));

			if(reportChannel == null)
			{
				new EmbedGenerator(channel).text("There is no report channel setup").color(Color.RED).sendTemporary();
				return;
			}

			if(reportedUser.equals(author)) Embed_Utils.sendError(channel, "You can't report yourself!");
			else if(args.length <= 1) Embed_Utils.sendError(channel, "Please mention a person & write a report topic.");
			else if(reportedUser.isBot()) Embed_Utils.sendError(channel, "You may not report bots.");
			else if(reportedMember.isOwner()) Embed_Utils.sendError(channel, "You may not report the owner.");
			else if(Yaml_Utils.isFieldEmpty(guild.getId() + ".reportchannel", "guild")) Embed_Utils.sendError(channel, "There is no report channel setup.");
			else
			{
				for(Message selectedMessage : channel.getHistory().retrievePast(5).complete())
				{
					if(selectedMessage.getAuthor().getId().equals(reportedMember.getId()))
					{
						messageLog.append(reportedMember.getAsMention()).append(" | ").append(selectedMessage.getContentRaw()).append("\n");
					}
				}

				Arrays.stream(Array_Utils.depend(args, 0)).forEach(word -> reportDescription.append(" ").append(word));
				if(messageLog.length() == 0) messageLog.append("No recent messages found for this user.");
				EmbedGenerator embed = new EmbedGenerator(reportChannel)
						.title("New report by: " + author.getAsTag())
						.element("Reporting user:", reportedMember.getAsMention())
						.element("Description:", reportDescription.toString())
						.element("Channel:", String_Utils.getChannelAsMention(channel.getId()))
						.element("Message Log:", messageLog.toString())
						.color(reportedMember.getColor())
						.footer("This report is unhandled and can only be dealt by members higher than " + User_Utils.getMemberFromUser(author, guild).getRoles().get(0).getName());

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
		else Embed_Utils.sendError(channel, "Could not find the user " + args[0] + ".");
	}
}
