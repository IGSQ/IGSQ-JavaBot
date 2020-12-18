package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EventWaiter;

public class MessageReactionAddEvent_Main extends ListenerAdapter
{
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		EventWaiter.waitingOnThis(event);
	}
}
  


