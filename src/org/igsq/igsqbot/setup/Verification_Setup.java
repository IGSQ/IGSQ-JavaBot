package org.igsq.igsqbot.setup;

import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.GUIGenerator;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Verification_Setup 
{
	private MessageReceivedEvent event;

	public Verification_Setup(MessageReceivedEvent event) 
	{
		this.event = event;
		new GUIGenerator(event.getChannel(), new EmbedGenerator(null).title("Are you gay?").text("1. Maybe\n2. Murray"));
	}
}
