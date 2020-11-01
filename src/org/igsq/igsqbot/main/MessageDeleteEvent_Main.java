package org.igsq.igsqbot.main;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageDeleteEvent_Main extends ListenerAdapter
{
	public MessageDeleteEvent_Main()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageDelete(MessageDeleteEvent event)
    {
		Yaml.updateField(event.getMessageId(), "internal", null);
    }
}
