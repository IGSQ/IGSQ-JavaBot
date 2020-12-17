package org.igsq.igsqbot.objects;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.util.ArrayList;
import java.util.List;

public class GUIGenerator
{
	private static final List<GUIGenerator> guiList = new ArrayList<>();
	private final EmbedGenerator embed;
	private Message message = null;
	private static final String NUMBER_CODEPOINT = "\u20E3";
	private GenericEvent event;

	public GUIGenerator(EmbedGenerator embed)
	{
		this.embed = embed;

	}

	public Boolean confirmation(User user, long timeout)
	{
		guiList.add(this);
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();

		for(String selectedReaction : Common.TICK_REACTIONS) message.addReaction(selectedReaction).queue();

		MessageReactionAddEvent reactionEvent;
		EventWaiter waiter = new EventWaiter();

		try
		{
			reactionEvent = waiter.waitFor(MessageReactionAddEvent.class, event -> event.getUser().equals(user) && !event.getUser().isBot(), timeout);
		}
		catch(Exception exception)
		{
			reactionEvent = null;
		}

		event = reactionEvent;
		if(reactionEvent != null)
		{
			User reactingUser = null;
			try
			{
				reactingUser = reactionEvent.retrieveUser().submit().get();
			}
			catch(Exception exception)
			{
				new ErrorHandler(exception);
				return null;
			}

			reactionEvent.getReaction().removeReaction(reactingUser).queue();
			if(reactionEvent.getReactionEmote().isEmoji())
			{
				if(reactionEvent.getReactionEmote().getAsCodepoints().equals("U+2705"))
				{
					return true;
				}
				else if(reactionEvent.getReactionEmote().getAsCodepoints().equals("U+274e"))
				{
					return false;
				}
				else
				{
					return confirmation(user, timeout);
				}
			}
			else
			{
				return confirmation(user, timeout);
			}
		}
		else
		{
			return null;
		}
	}

	public String input(User user, long timeout)
	{
		guiList.add(this);
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();

		MessageReceivedEvent messageEvent;
		EventWaiter waiter = new EventWaiter();

		try
		{
			messageEvent = waiter.waitFor(MessageReceivedEvent.class, event -> event.getAuthor().equals(user) && !event.getAuthor().isBot(), timeout);
		}
		catch(Exception exception)
		{
			messageEvent = null;
		}

		event = messageEvent;
		if(messageEvent != null)
		{

			return messageEvent.getMessage().getContentRaw();
		}
		else
		{
			return null;
		}
	}

	public int menu(User user, long timeout, int optionCount)
	{
		guiList.add(this);
		message = embed.getChannel().sendMessage(embed.getBuilder().build()).complete();

		if(optionCount > 10) optionCount = 10;
		for(int i = 1; i <= optionCount; i++)
		{
			message.addReaction(i + NUMBER_CODEPOINT).queue();
		}

		final MessageReactionAddEvent reactionEvent;
		EventWaiter waiter = new EventWaiter();
		try
		{
			reactionEvent = waiter.waitFor(MessageReactionAddEvent.class, event -> event.getUser().equals(user) && !event.getUser().isBot(), timeout);
		}
		catch(Exception exception)
		{
			return -1;
		}

		event = reactionEvent;
		if(reactionEvent != null)
		{

			reactionEvent.retrieveUser().queue(reactingUser -> reactionEvent.getReaction().removeReaction(reactingUser).queue(null, error ->
			{
			}));

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
						return menu(user, timeout, optionCount);
					}
				}
				catch(Exception exception)
				{
					return menu(user, timeout, optionCount);
				}
			}
			else
			{
				return menu(user, timeout, optionCount);
			}
		}
		else
		{
			message.delete().queue();
			return -1;
		}
	}

	public void close()
	{
		message.delete().queue();
	}

	public static void closeAll()
	{
		for(GUIGenerator generator : guiList)
		{
			generator.close();
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
