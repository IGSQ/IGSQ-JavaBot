package org.igsq.igsqbot;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

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
	
	public static boolean sendEmbed(String messageText,TextChannel channel) 
	{
		return sendEmbed(messageText,channel,Color.BLACK);
	}
	public static boolean sendEmbed(String messageText,TextChannel channel,String title,String footer) 
	{
		return sendEmbed(messageText,channel,title,footer,Color.BLACK);
	}
	public static boolean sendEmbed(Field[] fields,TextChannel channel,String title,String footer) 
	{
		return sendEmbed(fields,channel,title,footer,Color.BLACK);
	}
	public static boolean sendEmbed(Field[] fields,TextChannel channel,String title,String footer,String thumbnailUrl) 
	{
		return sendEmbed(fields,channel,title,footer,thumbnailUrl,Color.BLACK);
	}
	
	public static boolean sendTimedEmbed(String messageText,TextChannel channel,int time) 
	{
		return sendTimedEmbed(messageText,channel,Color.BLACK,time);
	}
	public static boolean sendTimedEmbed(String messageText,TextChannel channel,String title,String footer, int time) 
	{
		return sendTimedEmbed(messageText,channel,title,footer,Color.BLACK, time);
	}
	public static boolean sendTimedEmbed(Field[] fields,TextChannel channel,String title,String footer, int time) 
	{
		return sendTimedEmbed(fields,channel,title,footer,Color.BLACK, time);
	}
	public static boolean sendTimedEmbed(Field[] fields,TextChannel channel,String title,String footer,String thumbnailUrl, int time) 
	{
		return sendTimedEmbed(fields,channel,title,footer,Color.BLACK,thumbnailUrl, time);
	}
	public static boolean sendTimedEmbed(String messageText,TextChannel channel,Color color,int time) 
	{
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > Common.CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	        embedBuilder.setDescription(messageText);
	        embedBuilder.setColor(color);
	        channel.sendMessage(embedBuilder.build()).queue(
	        		message -> 
	        		{
	        			message.delete().submitAfter(time, TimeUnit.SECONDS).whenComplete((v, error) -> 
	        				{
	        	               if (error != null) {};
	        				}
	        			);;
	        		}
	        	);
	        return true;
		}
		return false;
	}
	public static boolean sendTimedEmbed(String messageText,TextChannel channel,String title,String footer,Color color,int time) 
	{
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > Common.CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	embedBuilder.setTitle(title);
	        embedBuilder.setColor(color);
	        embedBuilder.setFooter(footer);
	        channel.sendMessage(embedBuilder.build()).queue(
	        		message -> 
	        		{
	        			message.delete().submitAfter(time, TimeUnit.SECONDS).whenComplete((v, error) -> 
	        				{
	        	               if (error != null) {};
	        				}
	        			);;
	        		}
	        	);
	        return true;
		}
		return false;
	}
	public static boolean sendTimedEmbed(Field[] fields,TextChannel channel,String title,String footer,Color color, int time) 
	{
		if(channel == null) return false;
	//	if(messageText == null || messageText.equalsIgnoreCase("")) return false;
	//	if(messageText.length() > CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	embedBuilder.setTitle(title);
	    	for(Field field : fields) embedBuilder.addField(field);
	        embedBuilder.setColor(color);
	        embedBuilder.setFooter(footer);
	        channel.sendMessage(embedBuilder.build()).queue(
	        		message -> 
	        		{
	        			message.delete().submitAfter(time, TimeUnit.SECONDS).whenComplete((v, error) -> 
	        				{
	        	               if (error != null) {};
	        				}
	        			);;
	        		}
	        	);
	        return true;
		}
		return false;
	}
	public static boolean sendTimedEmbed(Field[] fields,TextChannel channel,String title,String footer,Color color, String thumbnailUrl,int time) 
	{
		if(channel == null) return false;
	//	if(messageText == null || messageText.equalsIgnoreCase("")) return false;
	//	if(messageText.length() > CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	embedBuilder.setTitle(title);
	    	for(Field field : fields) embedBuilder.addField(field);
	        embedBuilder.setColor(color);
	        embedBuilder.setFooter(footer);
	        embedBuilder.setThumbnail(thumbnailUrl);
	        channel.sendMessage(embedBuilder.build()).queue(
	        		message -> 
	        		{
	        			message.delete().submitAfter(time, TimeUnit.SECONDS).whenComplete((v, error) -> 
	        				{
	        	               if (error != null) {};
	        				}
	        			);;
	        		}
	        	);
	        return true;
		}
		return false;
	}
	
	public static boolean sendEmbed(String messageText,TextChannel channel,Color color) 
	{
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > Common.CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	        embedBuilder.setDescription(messageText);
	        embedBuilder.setColor(color);
	        channel.sendMessage(embedBuilder.build()).queue();
	        return true;
		}
		return false;
	}
	public static boolean sendDelayedEmbed(String messageText,TextChannel channel,Color color,int milliSeconds) 
	{
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > Common.CHARACTER_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	        embedBuilder.setDescription(messageText);
	        embedBuilder.setColor(color);
	        channel.sendMessage(embedBuilder.build()).queueAfter(milliSeconds,TimeUnit.MILLISECONDS);
	        return true;
		}
		return false;
	}
	public static boolean sendEmbed(String messageText,TextChannel channel,String title,String footer,Color color) 
	{
		if(channel == null) return false;
		if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		if(messageText.length() > Common.CHARACTER_LIMIT) return false;
		if(title.length() > Common.EMBED_TITLE_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	embedBuilder.setTitle(title);
	        embedBuilder.setDescription(messageText);
	        embedBuilder.setColor(color);
	        embedBuilder.setFooter(footer);
	        channel.sendMessage(embedBuilder.build()).queue();
	        return true;
		}
		return false;
	}
	public static boolean sendEmbed(Field[] fields,TextChannel channel,String title,String footer,Color color) 
	{
		if(channel == null) return false;
		//if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		//if(messageText.length() > CHARACTER_LIMIT) return false;
		if(title.length() > Common.EMBED_TITLE_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	embedBuilder.setTitle(title);
	    	for(Field field : fields) embedBuilder.addField(field);
	        embedBuilder.setColor(color);
	        embedBuilder.setFooter(footer);
	        channel.sendMessage(embedBuilder.build()).queue();
	        return true;
		}
		return false;
	}
	public static boolean sendEmbed(Field[] fields,TextChannel channel,String title,String footer,String thumbnailUrl,Color color) 
	{
		if(channel == null) return false;
		//if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		//if(messageText.length() > CHARACTER_LIMIT) return false;
		if(title.length() > Common.EMBED_TITLE_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	embedBuilder.setTitle(title);
	    	for(Field field : fields) embedBuilder.addField(field);
	        embedBuilder.setColor(color);
	        embedBuilder.setFooter(footer);
	        embedBuilder.setThumbnail(thumbnailUrl);
	        channel.sendMessage(embedBuilder.build()).queue();
	        return true;
		}
		return false;
	}
	
	public static boolean sendEmbed(Field[] fields,TextChannel channel,String title,String footer,String thumbnailUrl,Color color,String[] reactions) 
	{
		if(channel == null) return false;
		//if(messageText == null || messageText.equalsIgnoreCase("")) return false;
		//if(messageText.length() > CHARACTER_LIMIT) return false;
		if(title.length() > Common.EMBED_TITLE_LIMIT) return false;
		
		Member me = channel.getGuild().getSelfMember();
		if(me.hasPermission(Permission.MESSAGE_WRITE)) 
		{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	embedBuilder.setTitle(title);
	    	for(Field field : fields) embedBuilder.addField(field);
	        embedBuilder.setColor(color);
	        embedBuilder.setFooter(footer);
	        embedBuilder.setThumbnail(thumbnailUrl);
	        channel.sendMessage(embedBuilder.build()).queue
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
		
}
