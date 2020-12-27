package org.igsq.igsqbot.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

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

	public boolean hasPermission(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission((GuildChannel) event.getChannel(), permissions) || (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}
}
