package org.igsq.igsqbot;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
/**
 * Creates Message using JDA's {@link MessageBuilder} api, with increased functionality.
 * @see #Message(MessageChannel)
 * @see #text(String)
 * @see #reaction(String[])
 * @see #send()
 * @see #sendTemporary(int)
 * @see #getBuilder()
 * @see #getChannel()
 * @see #getReactions()
 */
public class MessageGenerator 
{
	private String[] reactions = {};
	private MessageChannel channel;
	private MessageBuilder message;
	/**
	 * Constructor for Message, requires a location for the message to be created in ({@link MessageChannel})
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public MessageGenerator(MessageChannel channel)
	{
		this.channel = channel;
		message = new MessageBuilder();
		message.allowMentions(MentionType.USER);
	}
	/**
	 * Adds a reaction to the message which is done after the message has been sent. The reactions can be retrieved at any time using {@link #getReactions()}. If more than {@link Messaging#REACTION_LIMIT} reactions exist they will be ignored.
	 * Overloads {@link #reaction(String[]) Multiple (Array)}.
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public MessageGenerator reaction(String emojiUnicode) 
	{
		if(reactions.length >= Messaging.REACTION_LIMIT) return this;
		reactions = Common.append(reactions, emojiUnicode);
		return this;
	}
	
	/**
	 * Adds an array of reactions to the message which is done after the message has been sent. The reactions can be retrieved at any time using {@link #getReactions()}. If more than {@link Messaging#REACTION_LIMIT} reactions exist they will be ignored.
	 * Overloads {@link #reaction(String) Singular}.
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public MessageGenerator reaction(String[] emojiUnicodes) 
	{
		for(String emojiUnicode : emojiUnicodes) 
		{
			if(reactions.length >= Messaging.REACTION_LIMIT) return this;
			reactions = Common.append(reactions, emojiUnicode);
		}
		return this;
	}
	
	
	/**
	 * Gets the reactions ready to be added to the message.
	 * @see  #reaction(String[])
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public String[] getReactions() 
	{
		return reactions;
	}
	/**
	 * Gets the channel the message will be send to, designated by the {@link #Message(MessageChannel) constructor}
	 * @see  net.dv8tion.jda.api.entities.MessageChannel
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public MessageChannel getChannel() 
	{
		return channel;
	}
	/**
	 * Gets the {@link MessageBuilder MessageBuilder} the message is being built from.
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public MessageBuilder getBuilder() 
	{
		return message;
	}
	/**
	 * Sets the text of the message.
	 * @see net.dv8tion.jda.api.MessageBuilder#setContent(String)
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public MessageGenerator text(String text) 
	{
		message.setContent(text);
		return this;
	}
	
	/**
	 * Sends the message to the channel designated in the {@link #Message(MessageChannel) constructor}.
	 * @see  net.dv8tion.jda.api.entities.MessageChannel#sendMessage(net.dv8tion.jda.api.entities.Message)
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public void send()
	{
		if(message.isEmpty()) return;
		if(channel instanceof TextChannel && !((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) return;
        channel.sendMessage(message.build()).queue
        (
				message -> 
				{
					if(channel instanceof TextChannel && ((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) for(String reaction : reactions) message.addReaction(reaction).queue();
				}
		);
	}
	/**
	 * Sends the message to the channel designated in the {@link #Message(MessageChannel) constructor}. Deletes the message after delay time has passed. Uses {@link TimeUnit#MILLISECONDS}.
	 * Overloads {@link #sendTemporary() default 10s}.
	 * @see  net.dv8tion.jda.api.entities.MessageChannel#sendMessage(MessageGenerator)
	 * @see  net.dv8tion.jda.api.entities.Message#delete()
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public void sendTemporary(int delay) 
	{
		if(message.isEmpty()) return;
		if(channel instanceof TextChannel && !((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) return;
		if(delay < 0) return;
		if(delay == 0) 
		{
			send();
			return;
		}
		channel.sendMessage(message.build()).queue
		(
    		message -> 
    		{
    			if(channel instanceof TextChannel && ((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) for(String reaction : reactions) message.addReaction(reaction).queue();
    			message.delete().submitAfter(delay, TimeUnit.MILLISECONDS);
    		}
        );
	}
	/**
	 * Sends the message to the channel designated in the {@link #Message(MessageChannel) constructor}. Deletes the message after 10 seconds has passed.
	 * Overloads {@link #sendTemporary(int) non default times}.
	 * @see  net.dv8tion.jda.api.entities.MessageChannel#sendMessage(MessageGenerator)
	 * @see  net.dv8tion.jda.api.entities.Message#delete()
	 * @see org.igsq.igsqbot.MessageGenerator
	 */
	public void sendTemporary() 
	{
		sendTemporary(10000);
	}
}
