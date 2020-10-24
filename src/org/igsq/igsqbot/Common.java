package org.igsq.igsqbot;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

public class Common {
	public static final int CHARACTER_LIMIT = 2048;
	public static final int REACTION_LIMIT = 20;
	public static final int EMBED_TITLE_LIMIT = 256;
	public static final String BOT_PREFIX = ".";
	public static final String[] STARTUP_MESSAGES = {"Hello and, again, welcome to the Aperture Science Computer-Aided Enrichment Center.",
			"Hello! This is the part where I kill you.",
			"I've been really busy being dead. You know, after you MURDERED ME.",
			"Am. Not. Dead! I'm not dead!",
			"I lie when I'm nervous.",
			""};
	
	public static JDABuilder jdaBuilder;
	public static JDA jda;
	public static SelfUser self;
	
	public static String[] depend(String[] array, int location)
    {
        String[] arrayDepended = new String[array.length-1];
        int hitRemove = 0;
        for (int i = 0;i < array.length;i++)
        {
            if(location != i){
                arrayDepended[i-hitRemove] = array[i];
            }
            else{
                hitRemove++;
            }
        }
        return arrayDepended;
    }
    public static String[] append(String[] array, String value)
    {
    	String[] arrayAppended = new String[array.length+1];
    	for (int i = 0;i < array.length;i++)
    	{
    		arrayAppended[i] = array[i];
    	}
    	arrayAppended[array.length] = value;
    	return arrayAppended;
    }
	
	
    /**
     * Removes all text before a given character. If the character is not found the whole string is returned.
     * @return <b>String</b>
     */
    public static String removeBeforeCharacter(String string,char target) 
    {
    	Boolean targetFound = false;
    	char[] charArray = string.toCharArray();
    	String rebuiltString = "";
    	for(int i = 0;i < string.length();i++) 
    	{
    		if(!targetFound)
    		{
    			if(charArray[i] == target) targetFound = true;
    		}
    		else rebuiltString += charArray[i];
    	}
    	if(targetFound) return rebuiltString;
    	else return string;
    }
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
    public static boolean sendEmbed(String messageText,TextChannel channel,Color color) 
    {
    	if(channel == null) return false;
    	if(messageText == null || messageText.equalsIgnoreCase("")) return false;
    	if(messageText.length() > CHARACTER_LIMIT) return false;
    	
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
    	if(messageText.length() > CHARACTER_LIMIT) return false;
    	
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
    	if(messageText.length() > CHARACTER_LIMIT) return false;
    	if(title.length() > EMBED_TITLE_LIMIT) return false;
    	
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
    	if(title.length() > EMBED_TITLE_LIMIT) return false;
    	
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
    	if(title.length() > EMBED_TITLE_LIMIT) return false;
    	
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
    	if(title.length() > EMBED_TITLE_LIMIT) return false;
    	
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
}
