package org.igsq.igsqbot.events;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.Yaml;

public class MessageDeleteEvent_Main extends ListenerAdapter
{
	@Override
    public void onMessageDelete(MessageDeleteEvent event)
    {
		Yaml.updateField(event.getMessageId(), "internal", null);
    }
}
