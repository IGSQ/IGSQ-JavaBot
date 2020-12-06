package org.igsq.igsqbot;

import org.igsq.igsqbot.util.EventWaiter;
import org.igsq.igsqbot.util.GUI_State;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class GUIGenerator
{
	private EmbedGenerator embed;
	private Message message = null;

	public GUIGenerator(EmbedGenerator embed) 
	{
		this.embed = embed;
	}
	
	public GUI_State confirmation(User user, long expirationTimeout)
	{	
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();
		
		for(String selectedReaction : Common.TICK_REACTIONS) message.addReaction(selectedReaction).queue();

		MessageReactionAddEvent reactionEvent;
		EventWaiter waiter = new EventWaiter();

		try
		{
			reactionEvent = waiter.waitFor(MessageReactionAddEvent.class, event -> event.getUser().equals(user) && !event.getUser().isBot(), expirationTimeout);
		} 
		catch (Exception exception) 
		{
			reactionEvent = null;
		}
		
		if(reactionEvent != null)
		{
			
			if(reactionEvent.getReactionEmote().isEmoji())
			{
				reactionEvent.getReaction().removeReaction(reactionEvent.getUser()).queue();
				if(reactionEvent.getReactionEmote().getAsCodepoints().equals("U+2705"))
				{
					return GUI_State.TRUE;
				}
				else if(reactionEvent.getReactionEmote().getAsCodepoints().equals("U+274e"))
				{
					return GUI_State.FALSE;
				}
				else
				{
					return GUI_State.INVALID_EMOJI;
				}
			}
			else
			{
				reactionEvent.getReaction().removeReaction(reactionEvent.getUser()).queue();
				return GUI_State.INVALID_EMOJI;
			}
		}
		else
		{
			return GUI_State.TIMEOUT;
		}
	}
	
	public String input(User user, long expirationTimeout)
	{	
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();

		MessageReceivedEvent messageEvent;
		EventWaiter waiter = new EventWaiter();

		try
		{
			messageEvent = waiter.waitFor(MessageReceivedEvent.class, event -> event.getAuthor().equals(user) && !event.getAuthor().isBot(), expirationTimeout);
		} 
		catch (Exception exception) 
		{
			messageEvent = null;
		}
		
		if(messageEvent != null)
		{
			return messageEvent.getMessage().getContentRaw();
		}
		else
		{
			return null;
		}
	}
	
	public Message getMessage()
	{
		return message;
	}
}
