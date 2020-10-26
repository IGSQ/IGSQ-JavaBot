package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.Common;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Clear_Command 
{
	private TextChannel channel;
	private User author;
	private Member guildAuthor;
	private Message message;
	private Member me;
	private String[] args;
	private int amount;
	final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public Clear_Command(MessageReceivedEvent event, String[] args)
	{
		this.author = event.getAuthor();
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		this.guildAuthor = event.getMember();
		this.me = event.getGuild().getSelfMember();
		this.args = args;
		
		clearQuery();
	}

	private void clearQuery()
	{
		if(!author.isBot() && message.isFromType(ChannelType.TEXT) && guildAuthor.hasPermission(Permission.MESSAGE_MANAGE) && me.hasPermission(Permission.MESSAGE_MANAGE)) clear();
		else Common.sendEmbed("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.",channel,Color.RED);
	
	}

	private void clear()
	{
		try 
		{
			amount = Integer.parseInt(args[0]);
		}
		
		catch(Exception e)
		{
			Common.sendEmbed("Enter a number as the amount.", channel);
			return;
		}
		
		if(amount == 0)
		{
			Common.sendEmbed("You must enter the amount of messages to clear!", channel);
			return;
		}
		
		else if(amount > 15)
		{
			Common.sendEmbed("You tried to delete too many messages (Limit: 15)", channel);
			return;
		}
		else
		{
			channel.getHistory().retrievePast(Integer.parseInt(args[0])).queue
			(
			messages ->
				{
					Common.sendTimedEmbed("Deleting " + amount + " messages", channel, 10);
					for(Message selectedMessage : messages)
					{
						try
						{
							scheduler.schedule(new Runnable()
							{

								@Override
								public void run() 
								{
									selectedMessage.delete().queue();
								} 	
								
					    	}, 3,TimeUnit.SECONDS);
								     
						}
						catch(Exception exception)
						{
							continue;
						}
						
					}
				}
			);
		}
	}
}
