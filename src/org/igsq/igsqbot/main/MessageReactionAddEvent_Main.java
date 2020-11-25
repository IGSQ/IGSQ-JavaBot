package org.igsq.igsqbot.main;

import org.igsq.igsqbot.Common;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Main extends ListenerAdapter 
{
	public MessageReactionAddEvent_Main()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		if(!(event.getUser() == null) && !event.getUser().isBot())
		{
			
		}
    }
}
  


