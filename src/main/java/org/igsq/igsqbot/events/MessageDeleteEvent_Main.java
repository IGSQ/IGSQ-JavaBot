package main.java.org.igsq.igsqbot.events;

import main.java.org.igsq.igsqbot.Yaml;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageDeleteEvent_Main extends ListenerAdapter
{
	@Override
    public void onMessageDelete(MessageDeleteEvent event)
    {
		Yaml.updateField(event.getMessageId(), "internal", null);
    }
}
