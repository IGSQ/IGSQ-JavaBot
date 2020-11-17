package org.igsq.igsqbot.main;

import org.igsq.igsqbot.Common;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedEvent_Main extends ListenerAdapter
{	
	public MessageReceivedEvent_Main()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {	
		if(event.getAuthor().isBot())
		{
			Message message2 = event.getMessage();
			System.out.println(message2);
		}
    }
}
