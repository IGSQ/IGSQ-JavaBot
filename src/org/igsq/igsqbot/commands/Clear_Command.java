package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Cooldown_Handler;

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
	private Cooldown_Handler handler;
	final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public Clear_Command(MessageReceivedEvent event, String[] args)
	{
		this.handler = Main_Command.getHandler(event.getGuild());
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
			amount = Integer.parseInt(args[0]) + 1;
		}
		
		catch(Exception exception)
		{
			Common.sendTimedEmbed("Enter a number as the amount.", channel, Color.RED, 10);
			return;
		}
		if(handler.isCooldownActive("clear"))
		{
			Common.sendTimedEmbed("This command is on cooldown. (Remaining: " + handler.getCooldown("clear") / 1000 + "s)", channel, Color.RED, 10);
			return;
		}
		else if(amount <= 0)
		{
			Common.sendTimedEmbed("Invalid amount entered.", channel, Color.RED, 10);
			return;
		}
		else if(amount > 49)
		{
			Common.sendTimedEmbed("You tried to delete too many messages (Limit: 50)", channel, Color.RED, 10);
			return;
		}
		else
		{
			new Thread(() -> 
			{
				handler.createCooldown("clear", 5000);
                List<Message> messages = channel.getHistory().retrievePast(amount).complete();
                try 
                {
                	channel.deleteMessages(messages).complete();
                    Common.sendTimedEmbed("Deleted " + (messages.size()) + " messages", channel, 5);
                }
                catch (Exception exception)
                {
                	Common.sendTimedEmbed("Messages in the channel is less than the amount specified.", channel, Color.RED, 10);
                }
	        }
	        )
	        .run();
		}
	}
}
