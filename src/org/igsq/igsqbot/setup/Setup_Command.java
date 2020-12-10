package org.igsq.igsqbot.setup;

import java.awt.Color;

import org.igsq.igsqbot.objects.EmbedGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Setup_Command 
{
	private final MessageChannel channel;
	private final User author;
	private final Member member;
	private final String[] args;
	private final MessageReceivedEvent event;

	public Setup_Command(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.member = event.getMember();
		this.args = event.getMessage().getContentRaw().toLowerCase().split(" ");
		this.event = event;
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
			case "verification":
				new Verification_Setup(event);
			case "logging":
			case "log":
				break;
			default:
				new EmbedGenerator(channel).text("That is an invalid setup parameter").color(Color.RED).sendTemporary();
		}
	}
}


