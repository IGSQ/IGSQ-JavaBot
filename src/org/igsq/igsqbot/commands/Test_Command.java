package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.ErrorHandler;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Test_Command {
	private final MessageChannel channel;
	private final User author;

	public Test_Command(MessageReceivedEvent event) 
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
//		GUIGenerator generator = new GUIGenerator(new EmbedGenerator(channel).text("Are you okay"));
//		Integer testState = generator.menu(author, 60000, 5);
//		if(testState == null)
//		{
//			new EmbedGenerator(null).text("Timeout").replace(generator.getMessage());
//			generator.getMessage().delete().queueAfter(5000, TimeUnit.MILLISECONDS);
//			test();
//		}
//		else
//		{
//			new EmbedGenerator(null).text("" + testState).replace(generator.getMessage());
//		}

		ErrorHandler.causeException();
	}
}
