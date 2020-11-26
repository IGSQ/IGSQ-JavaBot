package org.igsq.igsqbot.setup;

import java.awt.Color;

import org.igsq.igsqbot.EmbedGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Setup_Command 
{
	private MessageChannel channel;
	private User author;
	private Member member;
	private String[] args;
	private MessageReceivedEvent event;
	public Setup_Command(MessageReceivedEvent event, String[] args)
	{
		this.event = event;
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.member = event.getMember();
		this.args = args;
		setupQuery();
	}
	
	private void setupQuery()
	{
		if(!author.isBot() && member.hasPermission(Permission.MESSAGE_MANAGE)) setup();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void setup()
	{
		String action = "";
		try
		{
			action = args[0];
		}
		catch(Exception exception) 
		{
			new EmbedGenerator(channel).text("You entered an invalid action").sendTemporary(); 
			return;
		}
		
		switch(action.toLowerCase()) 
		{
			case "verify":
				new Verification_Setup(event);
				break;
			default:
				new EmbedGenerator(channel).text("You entered an invalid action").sendTemporary(); 
				return;
				
		}
		
	}
}
