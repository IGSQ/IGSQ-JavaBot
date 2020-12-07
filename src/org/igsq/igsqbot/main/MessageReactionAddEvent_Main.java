package org.igsq.igsqbot.main;

import org.igsq.igsqbot.util.EventWaiter;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Main extends ListenerAdapter 
{
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		EventWaiter.waitingOnThis(event);
    }
}
  


