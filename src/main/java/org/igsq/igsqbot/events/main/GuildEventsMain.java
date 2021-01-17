package org.igsq.igsqbot.events.main;

import java.util.List;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.util.DatabaseUtils;

public class GuildEventsMain extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public GuildEventsMain(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onGuildLeave(GuildLeaveEvent event)
	{
		DatabaseUtils.removeGuild(event.getGuild(), igsqBot);
	}

	@Override
	public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
	{
		DatabaseUtils.removeGuild(event.getGuildIdLong(), igsqBot);
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event)
	{
		DatabaseUtils.registerGuild(event.getGuild(), igsqBot);
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), igsqBot);

		for(ReactionRole reactionRole : reactionRoles)
		{
			if(reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				event.retrieveMember().queue(reactionRole::addRole);
			}
		}
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
	{
		List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(event.getMessageIdLong(), igsqBot);

		for(ReactionRole reactionRole : reactionRoles)
		{
			if(reactionRole.getEmote().equals(event.getReactionEmote().getName()))
			{
				event.retrieveMember().queue(reactionRole::removeRole);
			}
		}
	}
}

