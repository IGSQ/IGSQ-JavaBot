package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.EventWaiter;
import org.igsq.igsqbot.entities.cache.CachedMessage;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.GuildConfig;
import org.igsq.igsqbot.entities.cache.GuildConfigCache;
import org.igsq.igsqbot.entities.json.ReactionRole;
import org.igsq.igsqbot.util.YamlUtils;

public class MessageEventsMain extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageEventsMain(IGSQBot igsqBot) 
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		if(!EventWaiter.waitingOnThis(event) && event.isFromType(ChannelType.TEXT))
		{
			event.retrieveUser().queue(
					user ->
					{
						if(!user.isBot())
						{
							GuildConfig guildConfig = GuildConfigCache.getInstance().get(event.getGuild().getId());
							String emoteId = event.getReactionEmote().isEmoji() ? event.getReactionEmote().getEmoji() : event.getReactionEmote().getEmote().getId();
							for(ReactionRole reactionRole : guildConfig.getReactionRoles())
							{
								if(reactionRole.getPrimaryKey().replace("\\\\", "\\").equalsIgnoreCase(emoteId))
								{
									Role role = event.getGuild().getRoleById(reactionRole.getRole());
									if(role != null)
									{
										event.retrieveMember().queue(member -> event.getGuild().addRoleToMember(member, role).queue());
									}
								}
							}
						}
					}
			);
		}
	}



	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event)
	{
		if(!EventWaiter.waitingOnThis(event) && event.isFromType(ChannelType.TEXT))
		{
			event.retrieveUser().queue(
					user ->
					{
						if(!user.isBot())
						{
							GuildConfig guildConfig = GuildConfigCache.getInstance().get(event.getGuild().getId());
							String emoteId = event.getReactionEmote().isEmoji() ? event.getReactionEmote().getEmoji() : event.getReactionEmote().getEmote().getId();
							for(ReactionRole reactionRole : guildConfig.getReactionRoles())
							{
								if(reactionRole.getPrimaryKey().equals(emoteId))
								{
									Role role = event.getGuild().getRoleById(reactionRole.getRole());
									if(role != null)
									{
										event.retrieveMember().queue(member -> event.getGuild().removeRoleFromMember(member, role).queue());
									}
								}
							}
						}
					}
			);
		}
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		if(event.getChannelType().equals(ChannelType.TEXT) && !event.getAuthor().isBot() && !EventWaiter.waitingOnThis(event))
		{
			if(!event.getMessage().getContentRaw().startsWith(new org.igsq.igsqbot.entities.yaml.GuildConfig(event.getGuild(), event.getJDA()).getGuildPrefix()))
			{
				MessageCache.getCache(event.getGuild()).set(new CachedMessage(event.getMessage()));
			}
		}
		igsqBot.getCommandHandler().handle(event);
	}


	@Override
	public void onMessageDelete(MessageDeleteEvent event)
	{
		YamlUtils.clearField(event.getMessageId(), Filename.INTERNAL);
	}
}

