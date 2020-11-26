package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.GUIGenerator;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Test_Command {
	private MessageChannel channel;
	private User author;

	public Test_Command(MessageReceivedEvent event,String[] args) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		
		testQuery();
	}
	
	private void testQuery()
	{
		if(!author.isBot()) test();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void test() 
	{
		if(new GUIGenerator(new EmbedGenerator(channel).text("This is a test")).confirmation(author))
		{
			System.out.println("test returned true");
		}
		else
		{
			System.out.println("test returned false");
		}
	}
}
