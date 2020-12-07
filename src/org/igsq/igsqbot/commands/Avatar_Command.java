package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.EmbedGenerator;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Avatar_Command {
	private final MessageChannel channel;
	private final User author;
	private final Message message;
	private final MessageReceivedEvent event;

	public Avatar_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.message = event.getMessage();
		this.event = event;
		
		avatarQuery();
	}
	
	private void avatarQuery()
	{
		if(!author.isBot()) avatar();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void avatar() 
	{
		if(event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE))
		{
			if(message.getMentionedUsers().isEmpty() && message.getMentionedUsers().size() <= 3)
			{
				for(User selectedUser : message.getMentionedUsers())
				{
					new EmbedGenerator(channel)
					.title(selectedUser.getName() + "'s Avatar")
					.image(selectedUser.getEffectiveAvatarUrl())
					.color(Color.BLUE).send();
				}	
			}
			else if(message.getMentionedUsers().size() > 3)
			{
				new EmbedGenerator(channel).text("Too many users entered! (Limit: 3)").color(Color.RED).sendTemporary();
			}
			else
			{		
		    	EmbedBuilder embedBuilder = new EmbedBuilder();
		    	embedBuilder.setTitle(author.getName() + "'s Avatar");
		        embedBuilder.setImage(author.getAvatarUrl());
		        channel.sendMessage(embedBuilder.build()).queue();
			}
		}
	}
}
