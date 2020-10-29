package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Messaging;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Avatar_Command {
	private Member me;
	private TextChannel channel;
	private User author;
	private Message message;
	User avatarUser;
	private String avatarUrl = "";

	public Avatar_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getTextChannel();
		this.me = event.getGuild().getSelfMember();
		this.message = event.getMessage();
		
		avatarQuery();
	}
	
	private void avatarQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && !author.isBot()) avatar();
		else Messaging.sendEmbed("You cannot Execute this comman﻿d!\nThis may be due to sending it in the wrong channel or not having the required permission.",channel,Color.RED);
	}
	
	private void avatar() 
	{
		if(me.hasPermission(Permission.MESSAGE_WRITE))
		{
			if(!message.getMentionedUsers().isEmpty() && message.getMentionedUsers().size() <= 3)
			{
				for(User selectedUser : message.getMentionedUsers())
				{
					String selectedAvatar = selectedUser.getEffectiveAvatarUrl();
					
			    	EmbedBuilder embedBuilder = new EmbedBuilder();
			    	embedBuilder.setTitle(selectedUser.getName() + "\'s Avatar");
			        embedBuilder.setImage(selectedAvatar);
			        channel.sendMessage(embedBuilder.build()).queue();
				}
			}
			else if(message.getMentionedUsers().size() > 3)
			{
				Messaging.sendEmbed("Too many users entered! (Limit: 3)",channel,Color.RED);
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
