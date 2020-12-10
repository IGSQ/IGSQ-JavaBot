package org.igsq.igsqbot.objects;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Context
{
	private final MessageReceivedEvent event;

	public Context(MessageReceivedEvent event)
	{
		this.event = event;
	}

	public MessageChannel getChannel()
	{
		return event.getChannel();
	}

	public Message getMessage()
	{
		return event.getMessage();
	}

	public Guild getGuild()
	{
		return event.getGuild();
	}

	public User getAuthor()
	{
		return event.getAuthor();
	}
}
