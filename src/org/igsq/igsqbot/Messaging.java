package org.igsq.igsqbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class Messaging
{
	public static boolean sendMessage(String messageText,TextChannel channel,String[] reactions) 
	{
		if(reactions == null || reactions.length == 0) return sendMessage(messageText,channel);
		if(reactions.length > Common.REACTION_LIMIT) return false;
		
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > Common.CHARACTER_LIMIT) return false;
		
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
	public static boolean sendMessage(String messageText,TextChannel channel) 
	{
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > Common.CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
			channel.sendMessage(messageText.replace("@everyone", "everyone").replace("@here", "here")).queue();
			return true;
		}
		return false;
	}
	public static Embed embed(MessageChannel channel) 
	{
		return new Embed(channel);
		
	}
		
}
