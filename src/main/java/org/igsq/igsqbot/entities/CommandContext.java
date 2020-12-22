package org.igsq.igsqbot.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandContext
{
	private final MessageReceivedEvent event;

	public CommandContext(MessageReceivedEvent event)
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

	public JDA getJDA()
	{
		return event.getJDA();
	}

	public ChannelType getChannelType()
	{
		return event.getChannelType();
	}

	public MessageReceivedEvent getEvent()
	{
		return event;
	}
}
