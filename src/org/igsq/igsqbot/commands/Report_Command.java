package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Report_Command 
{
	private String[] args;
	private Message message;
	private User author;
	private MessageChannel channel;
	private MessageChannel reportChannel;
	private Guild guild;
	private String messageLog= "";
	private String reportDescription = "";
	private Member reportedMember;
	private Member member;
	
	public Report_Command(MessageReceivedEvent event, String[] args)
	{
		this.args = args;
		this.message = event.getMessage();
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.guild = event.getGuild();
		this.member = event.getMember();
		
		reportQuery();
	}
	
	private void reportQuery()
	{
		if(!author.isBot() && message.isFromType(ChannelType.TEXT)) report();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();	
	}
	
	private void report()
	{
		if(!message.getMentionedRoles().isEmpty() || message.getMentionedUsers().size() > 1)
		{
			new EmbedGenerator(channel).text("You cannot report multiple people.").color(Color.RED).sendTemporary();
		}
		else if(message.getMentionedMembers().isEmpty())
		{
			new EmbedGenerator(channel).text("You must enter someone to report!").color(Color.RED).sendTemporary();
		}
		else if(message.getMentionedUsers().get(0).isBot())
		{
			new EmbedGenerator(channel).text("You may not report bots.").color(Color.RED).sendTemporary();
		}
		else
		{
			reportedMember = message.getMentionedMembers().get(0);
			if(reportedMember.isOwner())
			{
				new EmbedGenerator(channel).text("You may not report the owner.").color(Color.RED).sendTemporary();
			}
			else if(args.length < 2)
			{
				new EmbedGenerator(channel).text("You must enter a report topic.").color(Color.RED).sendTemporary();
			}
			else if(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild") == null || Yaml.getFieldString(guild.getId() + ".reportchannel", "guild").isEmpty())
			{
				new EmbedGenerator(channel).text("There is no report channel setup.").color(Color.RED).sendTemporary(); // This should log to admins
			}
			else if(reportedMember.equals(member))
			{
				new EmbedGenerator(channel).text("You may not report yourself.").color(Color.RED).sendTemporary();
			}
			else
			{
				reportChannel = Common.jda.getTextChannelById(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild"));
				for(int i = 1; i < args.length; i++) reportDescription += args[i] + " ";
				
				for(Message selectedMessage : channel.getHistory().retrievePast(5).complete()) if(selectedMessage.getAuthor().getId().equals(reportedMember.getId())) messageLog += reportedMember.getAsMention() + " | " + selectedMessage.getContentRaw() + "\n";
				if(messageLog.isEmpty()) messageLog = "No recent messages found for this user.";
				EmbedGenerator embed = new EmbedGenerator(reportChannel)
				.title("New report by: " + author.getAsTag())
				.element("Reporting user:", reportedMember.getAsMention())
				.element("Description:", reportDescription)
				.element("Channel:", Common.getChannelAsMention(channel.getId()))
				.element("Message Log:", messageLog)
				.color(reportedMember.getColor())
				.footer("This report is unhandled and can only be dealt by members higher than "+ guild.retrieveMember(author).complete().getRoles().get(0).getName());
				
				reportChannel.sendMessage(embed.getBuilder().build()).queue
				(
						message ->
						{
							Yaml.updateField(message.getId() + ".report.reporteduser", "internal", reportedMember.getId());
							Yaml.updateField(message.getId() + ".report.reportinguser", "internal", author.getId());
							Yaml.updateField(message.getId() + ".report.enabled", "internal", true);
							message.addReaction("U+2705").queue();
						}
				);
				
			}	
		}
	}	
}
