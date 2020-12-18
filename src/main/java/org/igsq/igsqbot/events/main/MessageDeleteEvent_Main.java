package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.util.YamlUtils;

public class MessageDeleteEvent_Main extends ListenerAdapter
{
	@Override
	public void onMessageDelete(MessageDeleteEvent event)
	{
		YamlUtils.clearField(event.getMessageId(), "internal");
	}
}
