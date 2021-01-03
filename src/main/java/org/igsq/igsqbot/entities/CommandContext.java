package org.igsq.igsqbot.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.BotConfig;
import org.igsq.igsqbot.util.EmbedUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class CommandContext
{
	private final MessageReceivedEvent event;
	private final IGSQBot igsqBot;

	public CommandContext(MessageReceivedEvent event, IGSQBot igsqBot)
	{
		this.event = event;
		this.igsqBot = igsqBot;
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
		return event.isFromGuild() ? event.getGuild() : null;

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

	public void replyError(String errorText)
	{
		EmbedUtils.sendError(getChannel(), errorText);
	}

	public void replySuccess(String successText)
	{
		EmbedUtils.sendSuccess(getChannel(), successText);
	}

	public boolean isDeveloper()
	{
		BotConfig config = Json.get(BotConfig.class, Filename.CONFIG);
		if(config == null)
		{
			return false;
		}
		else
		{
			return config.getPrivilegedUsers().contains(getAuthor().getId());
		}
	}

	public IGSQBot getIGSQBot()
	{
		return igsqBot;
	}

	public boolean hasPermission(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission((GuildChannel) event.getChannel(), permissions) || (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}
}
