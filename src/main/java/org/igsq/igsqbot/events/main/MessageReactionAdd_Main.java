package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.entities.EventWaiter;
import org.igsq.igsqbot.entities.yaml.ReactionRole;

import java.util.List;

public class MessageReactionAdd_Main extends ListenerAdapter
{
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
							ReactionRole rr = new ReactionRole(event.getGuild().getId(), event.getChannel().getId(), event.getMessageId());
							List<Role> rrRoles = rr.getRoles(event.getReactionEmote().isEmoji() ? event.getReactionEmote().getEmoji() : event.getReactionEmote().getEmote().getId(), event.getGuild());
							event.retrieveMember().queue(member -> rrRoles.forEach(role -> event.getGuild().addRoleToMember(member.getId(), role).queue()));
						}
					}
			);
		}
	}
}
  


