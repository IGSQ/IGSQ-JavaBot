package org.igsq.igsqbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class Messaging
{
	public static final int CHARACTER_LIMIT = 2048;
	public static final int REACTION_LIMIT = 20;
	public static final int EMBED_TITLE_LIMIT = 256;
	@Deprecated
	public static boolean sendMessage(String messageText,TextChannel channel,String[] reactions) 
	{
		if(reactions == null || reactions.length == 0) return sendMessage(messageText,channel);
		if(reactions.length > REACTION_LIMIT) return false;
		
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
			channel.sendMessage(messageText.replace("@everyone", "everyone").replace("@here", "here")).queue
			(
					message -> 
					{
						for(String reaction : reactions) message.addReaction(reaction).queue();
					}
			);
			return true;
		}
		return false;
	}
	@Deprecated
	public static boolean sendMessage(String messageText,TextChannel channel) 
	{
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
			channel.sendMessage(messageText.replace("@everyone", "everyone").replace("@here", "here")).queue();
			return true;
		}
		return false;
	}
	public static EmbedGenerator embed(MessageChannel channel) 
	{
		return new EmbedGenerator(channel);
		
	}
	
	public static MessageGenerator message(MessageChannel channel) 
	{
		return new MessageGenerator(channel);
		
	}
		
}
