package org.igsq.igsqbot.objects;

import net.dv8tion.jda.api.events.GenericEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.GUI_State;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class GUIGenerator
{
	private final EmbedGenerator embed;
	private Message message = null;
	private static final String NUMBER_CODEPOINT = "\u20E3";
	private GenericEvent event;

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
			User reactingUser = null;
			try
			{
				reactingUser = reactionEvent.retrieveUser().submit().get();
			}
			catch (Exception exception)
			{
				new ErrorHandler(exception);
				return GUI_State.EXCEPTION;
			}

			reactionEvent.getReaction().removeReaction(reactingUser).queue();
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
		event = null;

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
			event = messageEvent;
			return messageEvent.getMessage().getContentRaw();
		}
		else
		{
			return null;
		}
	}
	
	public int menu(User user, long timeout, int optionCount)
	{
		for(int i = 1; i <= optionCount; i++)
		{
			message.addReaction(i + NUMBER_CODEPOINT).queue();
		}

		MessageReactionAddEvent reactionEvent;
		EventWaiter waiter = new EventWaiter();
		event = null;
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
			User reactingUser = null;
			try
			{
				reactingUser = reactionEvent.retrieveUser().submit().get();
			}
			catch (Exception exception)
			{
				new ErrorHandler(exception);
				return -1;
			}
			event = reactionEvent;
			reactionEvent.getReaction().removeReaction(reactingUser).queue(error -> {});
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
						return -1;
					}
				}
				catch(Exception exception)
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}
	
	public EmbedGenerator getEmbed()
	{
		return embed;
	}
	public Message getMessage()
	{
		return message;
	}

	public GenericEvent getEvent()
	{
		return event;
	}
}
