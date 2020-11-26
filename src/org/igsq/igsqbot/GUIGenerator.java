package org.igsq.igsqbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class GUIGenerator 
{
	private MessageChannel channel;
	private EmbedGenerator embed;
	private String[] options;
	private MessageReactionAddEvent event;
	
	public GUIGenerator(MessageChannel channel, EmbedGenerator embed) 
	{
		this.channel = channel;
		this.embed = embed;
	}
	public boolean confirmation(User user)
	{
		
		EventWaiter waiter = new EventWaiter(Common.scheduler,true);
		Common.jda.addEventListener(waiter);
		waiter.waitForEvent(MessageReactionAddEvent.class, event -> event.getUser().equals(user) && !event.getUser().isBot() && event.getChannel().equals(channel) ,event -> this.event = event);
		if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+2705"))
		{
			return true;
		}
		else if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+274E"))
		{
			return false;
		}
		else 
		{
			return confirmation(user);
		}
	}
	
	public boolean question(String[] options) 
	{
		return false;
	}
	public String list(String[] options)
	{
		return null;
	}
	
	public String userInput(String[] options)
	{
		return null;
	}
	
	
	
	
	
	
	public String[] getOptions() 
	{
		return options;
	}
	public MessageChannel getChannel() 
	{
		return channel;
	}
	public EmbedGenerator getEmbed() 
	{
		return embed;
	}
	public GUIGenerator setOptions(String[] options) 
	{
		this.options = options;
		return this;
	}
	public GUIGenerator setChannel(MessageChannel channel) 
	{
		this.channel = channel;
		return this;
	}
	public GUIGenerator setEmbed(EmbedGenerator embed) 
	{
		this.embed = embed;
		return this;
	}
}
