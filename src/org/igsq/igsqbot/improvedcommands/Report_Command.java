package org.igsq.igsqbot.improvedcommands;

import java.awt.Color;
import java.util.Arrays;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.Yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

		final User reportedUser = Common.getUserFromMention(args[0]);
		if(reportedUser != null)
		{
			Member reportedMember = Common.getMemberFromUser(reportedUser, guild);
			MessageChannel reportChannel = jda.getTextChannelById(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild"));

			if(reportChannel == null)
			{
				new EmbedGenerator(channel).text("There is no report channel setup").color(Color.RED).sendTemporary();
				return;
			}

			if(reportedUser.equals(author)) new EmbedGenerator(channel).text("You can't report yourself!").color(Color.RED).sendTemporary();
			else if(args.length <= 1) new EmbedGenerator(channel).text("Please mention a person & write a report topic.").color(Color.RED).sendTemporary();
			else if(reportedUser.isBot()) new EmbedGenerator(channel).text("You may not report bots.").color(Color.RED).sendTemporary();
			else if(reportedMember.isOwner()) new EmbedGenerator(channel).text("You may not report the owner.").color(Color.RED).sendTemporary();
			else if(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild") == null || Yaml.getFieldString(guild.getId() + ".reportchannel", "guild").isEmpty()) new EmbedGenerator(channel).text("There is no report channel setup.").color(Color.RED).sendTemporary(); // This should log to admins
			else
			{
				for(Message selectedMessage : channel.getHistory().retrievePast(5).complete())
				{
					if(selectedMessage.getAuthor().getId().equals(reportedMember.getId()))
					{
						messageLog.append(reportedMember.getAsMention()).append(" | ").append(selectedMessage.getContentRaw()).append("\n");
					}
				}

				Arrays.stream(Common.depend(args, 0)).forEach(word -> reportDescription.append(" ").append(word));
				if(messageLog.length() == 0) messageLog.append("No recent messages found for this user.");
				EmbedGenerator embed = new EmbedGenerator(reportChannel)
						.title("New report by: " + author.getAsTag())
						.element("Reporting user:", reportedMember.getAsMention())
						.element("Description:", reportDescription.toString())
						.element("Channel:", Common.getChannelAsMention(channel.getId()))
						.element("Message Log:", messageLog.toString())
						.color(reportedMember.getColor())
						.footer("This report is unhandled and can only be dealt by members higher than " + Common.getMemberFromUser(author, guild).getRoles().get(0).getName());

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
		else new EmbedGenerator(channel).text("Could not find the user " + args[0] + ".").color(Color.RED).sendTemporary();
	}
}
