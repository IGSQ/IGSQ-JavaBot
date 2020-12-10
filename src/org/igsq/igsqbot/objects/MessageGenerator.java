package org.igsq.igsqbot.objects;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.util.Messaging;

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
	private final MessageChannel channel;
	private final MessageBuilder messageB;
	private Message message = null;
	/**
	 * Constructor for Message, requires a location for the message to be created in ({@link MessageChannel})
	 * @see MessageGenerator
	 */
	public MessageGenerator(MessageChannel channel)
	{
		this.channel = channel;
		messageB = new MessageBuilder();
		messageB.allowMentions(MentionType.USER);
	}
	/**
	 * Adds a reaction to the message which is done after the message has been sent. The reactions can be retrieved at any time using {@link #getReactions()}. If more than {@link Messaging#REACTION_LIMIT} reactions exist they will be ignored.
	 * Overloads {@link #reaction(String[]) Multiple (Array)}.
	 * @see MessageGenerator
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
	 * @see MessageGenerator
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
	 * @see MessageGenerator
	 */
	public String[] getReactions() 
	{
		return reactions;
	}
	/**
	 * Gets the channel the message will be send to, designated by the {@link #Message(MessageChannel) constructor}
	 * @see  net.dv8tion.jda.api.entities.MessageChannel
	 * @see MessageGenerator
	 */
	public MessageChannel getChannel() 
	{
		return channel;
	}
	/**
	 * Gets the {@link MessageBuilder MessageBuilder} the message is being built from.
	 * @see MessageGenerator
	 */
	public MessageBuilder getBuilder() 
	{
		return messageB;
	}
	/**
	 * Sets the text of the message.
	 * @see net.dv8tion.jda.api.MessageBuilder#setContent(String)
	 * @see MessageGenerator
	 */
	public MessageGenerator text(String text) 
	{
		messageB.setContent(text);
		return this;
	}
	
	/**
	 * Sends the message to the channel designated in the {@link #Message(MessageChannel) constructor}.
	 * @see  net.dv8tion.jda.api.entities.MessageChannel#sendMessage(net.dv8tion.jda.api.entities.Message)
	 * @see MessageGenerator
	 */
	public Message send()
	{
		if(messageB.isEmpty()) return null;
		if(channel instanceof TextChannel && !((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) return null;
        channel.sendMessage(messageB.build()).queue
        (
				message -> 
				{
					if(channel instanceof TextChannel && ((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) for(String reaction : reactions) message.addReaction(reaction).queue();
					this.message = message;
				}
		);
        return message;
	}
	/**
	 * Sends the message to the channel designated in the {@link #Message(MessageChannel) constructor}. Deletes the message after delay time has passed. Uses {@link TimeUnit#MILLISECONDS}.
	 * Overloads {@link #sendTemporary() default 10s}.
	 * @return 
	 * @see  net.dv8tion.jda.api.entities.MessageChannel#sendMessage(MessageGenerator)
	 * @see  net.dv8tion.jda.api.entities.Message#delete()
	 * @see MessageGenerator
	 */
	public Message sendTemporary(int delay) 
	{
		if(messageB.isEmpty()) return null;
		if(channel instanceof TextChannel && !((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) return null;
		if(delay < 0) return null;
		if(delay == 0) return send();
		channel.sendMessage(messageB.build()).queue
		(
    		message -> 
    		{
    			if(channel instanceof TextChannel && ((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) for(String reaction : reactions) message.addReaction(reaction).queue();
    			message.delete().submitAfter(delay, TimeUnit.MILLISECONDS);
    			this.message = message;
    		}
        );
		return message;
	}
	/**
	 * Sends the message to the channel designated in the {@link #Message(MessageChannel) constructor}. Deletes the message after 10 seconds has passed.
	 * Overloads {@link #sendTemporary(int) non default times}.
	 * @return 
	 * @see  net.dv8tion.jda.api.entities.MessageChannel#sendMessage(MessageGenerator)
	 * @see  net.dv8tion.jda.api.entities.Message#delete()
	 * @see MessageGenerator
	 */
	public Message sendTemporary() 
	{
		return sendTemporary(10000);
	}
}
