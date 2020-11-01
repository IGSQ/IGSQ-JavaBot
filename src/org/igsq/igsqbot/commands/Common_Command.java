package org.igsq.igsqbot.commands;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class Common_Command 
{
	public static final String[] POLL_EMOJIS_UNICODE = {"U+1F350", " U+1F349", "U+1F34D", "U+1F34E", "U+1F34C", "U+1F951", "U+1F346", "U+1F95D", "U+1F347", "U+1FAD0", "U+1F352", "U+1F9C5", "U+1F351", "U+1F34B", "U+1F34A","U+1F348", "U+1F965", "U+1F9C4", "U+1F952", "U+1F991"};
	public static final String[] POLL_EMOJIS = {":pear:", ":watermelon:", ":pineapple:", ":apple:", ":banana:", ":avocado:", ":eggplant:", ":kiwi:", ":grapes:", ":blueberries:", ":cherries:", ":onion:", ":peach:", ":lemon:", ":tangerine:", ":melon:", ":coconut:",":garlic:", ":cucumber:", ":squid:"};
	
	public static final String[] SHUTDOWN_MESSAGES = {"Goodbye, Caroline.",
			"Federal Superfund regulations require us to inform you that you must now leave the theater, as measuring the effects of asbestos-lined promotional clothing is not part of today's presentation. Enjoy your free t-shirt. Goodbye.",
			"You know what, this plan is so good, I'm going to give you a sporting chance and turn off the neurotoxin. I'm joking. Of course. Goodbye.",
			"Shutting down",
			"Good news! I can use this equipment to shut down the neurotoxin system. It is, however, password protected. AH! Alarm bells! No. Don't worry. Not a problem for me.",
			"All reactor core safeguards are now non-functional. Please prepare for reactor core meltdown.",
			"Undelete, undelete! Where's the undelete button?",
			"Now then, let's see what we got here. Ah! 'Reactor Core Emergency Heat Venting Protocols.' That's the problem right there, isn't it? 'Emergency'. You don't want to see 'emergency' flashing at you. Never good that, is it? Right. DELETE.",
			"In order to ensure that sufficient power remains for core testing protocols, all safety devices have been disabled.",
			"Okay, listen, we should get our stories straight, alright? If anyone asks -- and no one's gonna ask, don't worry -- but if anyone asks, tell them as far as you know, the last time you checked, everyone looked pretty much alive. Alright? Not dead.",
			"Well. I suppose we could just sit in this room and glare at each other until somebody drops dead, but I have a better idea."};
    
    @Deprecated
	public static boolean sendPoll(Field[] fields,TextChannel channel,String title,String footer,String description,String thumbnailUrl,String[] reactions,Color color) 
    {
    	if(channel == null) return false;
    	
    	Member me = channel.getGuild().getSelfMember();
    	if(me.hasPermission(Permission.MESSAGE_WRITE)) 
    	{
	    	EmbedBuilder embedBuilder = new EmbedBuilder();
	    	for(Field field : fields) embedBuilder.addField(field);
	    	embedBuilder.setDescription(description);
	        embedBuilder.setColor(color);
	        embedBuilder.setTitle(title);
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
	
	public static Cooldown_Handler[] append(Cooldown_Handler[] array, Cooldown_Handler value)
	{
		Cooldown_Handler[] arrayAppended = new Cooldown_Handler[array.length+1];
		for (int i = 0;i < array.length;i++)
		{
			arrayAppended[i] = array[i];
		}
		arrayAppended[array.length] = value;
		return arrayAppended;
	}

	public static Cooldown_Handler[] depend(Cooldown_Handler[] array, Cooldown_Handler value)
	{
		Cooldown_Handler[] arrayDepended = new Cooldown_Handler[array.length-1];
	    int hitRemove = 0;
	    
	    for (int i = 0;i < array.length;i++)
	    {
	        if(!value.equals(array[i]))
	        {
	            arrayDepended[i-hitRemove] = array[i];
	        }
	        else
	        {
	            hitRemove++;
	        }
	    }
	    return arrayDepended;
	}
}
