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
	private static final String NUMBER_CODEPOINT = "\u20E3";

	public GUIGenerator(EmbedGenerator embed) 
	{
		this.embed = embed;
	}
	
	public GUI_State confirmation(User user, long timeout)
	{	
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();
		
		for(String selectedReaction : Common.TICK_REACTIONS) message.addReaction(selectedReaction).queue();

		MessageReactionAddEvent reactionEvent;
		EventWaiter waiter = new EventWaiter();

		try
		{
			reactionEvent = waiter.waitFor(MessageReactionAddEvent.class, event -> event.getUser().equals(user) && !event.getUser().isBot(), timeout);
		} 
		catch (Exception exception) 
		{
			reactionEvent = null;
		}
		
		if(reactionEvent != null)
		{
			reactionEvent.getReaction().removeReaction(reactionEvent.getUser()).queue();
			if(reactionEvent.getReactionEmote().isEmoji())
			{
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
				return GUI_State.INVALID_EMOJI;
			}
		}
		else
		{
			return GUI_State.TIMEOUT;
		}
	}
	
	public String input(User user, long timeout)
	{	
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();

		MessageReceivedEvent messageEvent;
		EventWaiter waiter = new EventWaiter();

		try
		{
			messageEvent = waiter.waitFor(MessageReceivedEvent.class, event -> event.getAuthor().equals(user) && !event.getAuthor().isBot(), timeout);
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
	
	public Integer menu(User user, long timeout, int optionCount)
	{
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();
		
		for(int i = 1; i < optionCount; i++)
		{
			message.addReaction(i + NUMBER_CODEPOINT).queue();
		}

		MessageReactionAddEvent reactionEvent;
		EventWaiter waiter = new EventWaiter();

		try
		{
			reactionEvent = waiter.waitFor(MessageReactionAddEvent.class, event -> event.getUser().equals(user) && !event.getUser().isBot(), timeout);
		} 
		catch (Exception exception) 
		{
			reactionEvent = null;
		}
		if(reactionEvent != null)
		{
			reactionEvent.getReaction().removeReaction(reactionEvent.getUser()).queue(error -> {});
			if(reactionEvent.getReactionEmote().isEmoji())
			{
				String reactionNumber = reactionEvent.getReactionEmote().getEmoji().substring(0, 1);
				try
				{
					if(Integer.parseInt(reactionNumber) <= optionCount)
					{
						return Integer.parseInt(reactionNumber);		
					}
					else
					{
						return null;
					}
				}
				catch(Exception exception)
				{
					return null;
				}
			}
			else
			{
				return null;
			}
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
