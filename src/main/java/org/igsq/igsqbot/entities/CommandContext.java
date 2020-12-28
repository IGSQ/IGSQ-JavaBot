package org.igsq.igsqbot.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

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
		if(event.isFromGuild())
		{
			return event.getGuild();
		}
		else
		{
			return null;
		}

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

	/**
	 * This member will not be null, due to previous checks
	 * @return the member for this context
	 */
	@Nonnull
	public Member getMember()
	{
		return Objects.requireNonNull(event.getMember());
	}


	public boolean hasPermission(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission((GuildChannel) event.getChannel(), permissions) || (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}
}
