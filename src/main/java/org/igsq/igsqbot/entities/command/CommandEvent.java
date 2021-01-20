package org.igsq.igsqbot.entities.command;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.bot.ConfigOption;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.util.EmbedUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandEvent
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandEvent.class);
	private final MessageReceivedEvent event;
	private final IGSQBot igsqBot;
	private final Command command;
	private final List<String> args;

	public CommandEvent(MessageReceivedEvent event, IGSQBot igsqBot, Command command, List<String> args)
	{
		this.event = event;
		this.igsqBot = igsqBot;
		this.command = command;
		this.args = args;
	}

	public List<String> getArgs()
	{
		return args;
	}

	public String getPrefix()
	{
		if(!isFromGuild())
		{
			return Constants.DEFAULT_BOT_PREFIX;
		}
		else
		{
			return new GuildConfig(this).getPrefix();
		}
	}

	public Member getSelfMember()
	{
		return getGuild().getSelfMember();
	}

	public void addErrorReaction()
	{
		getMessage().addReaction(Emoji.FAILURE.getAsReaction()).queue(
				success -> getMessage().removeReaction(Emoji.FAILURE.getAsReaction()).queueAfter(10, TimeUnit.SECONDS, null,
						error -> LOGGER.debug("A command exception occurred", error)),
				error -> LOGGER.debug("A command exception occurred", error));
	}

	public void addSuccessReaction()
	{
		getMessage().addReaction(Emoji.SUCCESS.getAsReaction()).queue(
				success -> getMessage().removeReaction(Emoji.SUCCESS.getAsReaction()).queueAfter(10, TimeUnit.SECONDS, null,
						error -> LOGGER.debug("A command exception occurred", error)),
				error -> LOGGER.debug("A command exception occurred", error));
	}

	public IGSQBot getIGSQBot()
	{
		return igsqBot;
	}

	public Command getCommand()
	{
		return command;
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

	public long getGuildIdLong()
	{
		return getGuild().getIdLong();
	}

	public DSLContext getDBContext()
	{
		return getIGSQBot().getDatabaseHandler().getContext();
	}

	public User getAuthor()
	{
		return event.getAuthor();
	}

	public JDA getJDA()
	{
		return event.getJDA();
	}

	public boolean isChild()
	{
		return command.getParent() != null;
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
	 *
	 * @return the member for this context
	 */
	@Nonnull
	public Member getMember()
	{
		return Objects.requireNonNull(event.getMember());
	}

	public void replyError(String errorText)
	{
		addErrorReaction();
		EmbedUtils.sendError(getChannel(), errorText);
	}

	public void replySuccess(String successText)
	{
		addSuccessReaction();
		EmbedUtils.sendSuccess(getChannel(), successText);
	}

	public boolean isDeveloper()
	{
		return List.of(igsqBot.getConfig().getString(ConfigOption.PRIVILEGEDUSERS).split(",")).contains(getAuthor().getId());
	}

	public boolean isFromGuild()
	{
		return event.isFromGuild();
	}

	public boolean memberPermissionCheck(List<Permission> permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	public boolean memberPermissionCheck(Set<Permission> permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	public boolean memberPermissionCheck(Permission... permissions)
	{
		return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
	}

	public boolean selfPermissionCheck(Permission... permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}

	public void sendMessage(EmbedBuilder embed)
	{
		addSuccessReaction();
		getChannel().sendMessage(embed.setColor(Constants.IGSQ_PURPLE).setTimestamp(Instant.now()).build()).queue();
	}

	public boolean selfPermissionCheck(List<Permission> permissions)
	{
		return event.getGuild().getSelfMember().hasPermission(permissions);
	}
}
