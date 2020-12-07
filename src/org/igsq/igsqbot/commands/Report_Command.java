package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Report_Command 
{
	private String[] args;
	private final Message message;
	private final User author;
	private TextChannel channel;
	private Guild guild;
	private String messageLog= "";
	private Member reportedMember = null;
	private final JDA jda;
	
	public Report_Command(MessageReceivedEvent event)
	{
		this.args = event.getMessage().getContentRaw().toLowerCase().split(" ", 3);
		this.args = Common.depend(args, 0);
		this.message = event.getMessage();
		this.author = event.getAuthor();
		this.jda = event.getJDA();
		if(event.getChannelType().equals(ChannelType.TEXT)) 
		{
			this.channel = event.getTextChannel();
			this.guild = event.getGuild();
			reportQuery();
		}
		else
		{
			new EmbedGenerator(event.getChannel()).text("This command can only be done in a guild.").color(Color.RED).sendTemporary();
		}
		
	}
	
	private void reportQuery()
	{
		if(!author.isBot() && message.isFromType(ChannelType.TEXT)) report();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();	
	}
	
	private void report()
	{
		User reportedUser = Common.getUserFromMention(args[0]);
		if(reportedUser != null) 
		{
			reportedMember = Common.getMemberFromUser(reportedUser, guild);
			if(reportedUser.equals(author)) new EmbedGenerator(channel).text("You can't report yourself!").color(Color.RED).sendTemporary();
			else if(args.length <= 1) new EmbedGenerator(channel).text("Please mention a person & write a report topic.").color(Color.RED).sendTemporary();
			else if(reportedUser.isBot()) new EmbedGenerator(channel).text("You may not report bots.").color(Color.RED).sendTemporary();
			else if(reportedMember.isOwner()) new EmbedGenerator(channel).text("You may not report the owner.").color(Color.RED).sendTemporary();
			else if(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild") == null || Yaml.getFieldString(guild.getId() + ".reportchannel", "guild").isEmpty()) new EmbedGenerator(channel).text("There is no report channel setup.").color(Color.RED).sendTemporary(); // This should log to admins
			else
			{
				MessageChannel reportChannel = jda.getTextChannelById(Yaml.getFieldString(guild.getId() + ".reportchannel", "guild"));
				
				for(Message selectedMessage : channel.getHistory().retrievePast(5).complete()) if(selectedMessage.getAuthor().getId().equals(reportedMember.getId())) messageLog += reportedMember.getAsMention() + " | " + selectedMessage.getContentRaw() + "\n";
				if(messageLog.isEmpty()) messageLog = "No recent messages found for this user.";
				EmbedGenerator embed = new EmbedGenerator(reportChannel)
				.title("New report by: " + author.getAsTag())
				.element("Reporting user:", reportedMember.getAsMention())
				.element("Description:", args[1])
				.element("Channel:", Common.getChannelAsMention(channel.getId()))
				.element("Message Log:", messageLog)
				.color(reportedMember.getColor())
				.footer("This report is unhandled and can only be dealt by members higher than "+ Common.getMemberFromUser(author, guild).getRoles().get(0).getName());
				
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
		else new EmbedGenerator(channel).text("Could not find the reported User " + args[0] + ".").color(Color.RED).sendTemporary();
	}	
}
