package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.EmbedGenerator;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Avatar_Command {
	private Member me = null;
	private MessageChannel channel;
	private User author;
	private Message message;
	User avatarUser;
	private String avatarUrl = "";

	public Avatar_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.message = event.getMessage();
		if(channel.getType().equals(ChannelType.TEXT)) 
		{
			this.me = event.getGuild().getSelfMember();
		}
		
		avatarQuery();
	}
	
	private void avatarQuery()
	{
		if(!author.isBot()) avatar();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void avatar() 
	{
		if(me == null || me.hasPermission(Permission.MESSAGE_WRITE))
		{
			if(!message.getMentionedUsers().isEmpty() && message.getMentionedUsers().size() <= 3)
			{
				for(User selectedUser : message.getMentionedUsers()) new EmbedGenerator(channel).title(selectedUser.getName() + "\'s Avatar").image(selectedUser.getEffectiveAvatarUrl()).color(Color.BLUE).send();
			}
			else if(message.getMentionedUsers().size() > 3)
			{
				new EmbedGenerator(channel).text("Too many users entered! (Limit: 3)").color(Color.RED).sendTemporary();
			}
			else
			{
				avatarUser = author;
				avatarUrl = author.getEffectiveAvatarUrl();
				
		    	EmbedBuilder embedBuilder = new EmbedBuilder();
		    	embedBuilder.setTitle(avatarUser.getName() + "\'s Avatar");
		        embedBuilder.setImage(avatarUrl);
		        channel.sendMessage(embedBuilder.build()).queue();
			}
			
		}

	}
}
