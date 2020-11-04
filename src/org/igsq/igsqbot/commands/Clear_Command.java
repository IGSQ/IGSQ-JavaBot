package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.igsq.igsqbot.EmbedGenerator;
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
	private Member guildAuthor = null;
	private Message message;
	private Member me = null;
	private String[] args;
	private int amount;
	private Cooldown_Handler handler;
	final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public Clear_Command(MessageReceivedEvent event, String[] args)
	{
		this.author = event.getAuthor();
		this.message = event.getMessage();
		this.args = args;
		if(event.getChannelType().equals(ChannelType.TEXT)) 
		{
			this.channel = event.getTextChannel();
			this.handler = Main_Command.getHandler(event.getGuild().getId());
			this.guildAuthor = event.getMember();
			this.me = event.getGuild().getSelfMember();
			clearQuery();
		}
		else
		{
			new EmbedGenerator(event.getChannel()).text("This Command Can Only be done in a guild.").color(Color.RED).sendTemporary();
		}
		
	}

	private void clearQuery()
	{
		if(!author.isBot() && message.isFromType(ChannelType.TEXT) && guildAuthor.hasPermission(Permission.MESSAGE_MANAGE) && me.hasPermission(Permission.MESSAGE_MANAGE)) clear();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	
	}

	private void clear()
	{
		try 
		{
			amount = Integer.parseInt(args[0]);
		}
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("Enter a number as the amount.").color(Color.RED).sendTemporary();
			return;
		}
		if(handler.isCooldownActive("clear"))
		{
			new EmbedGenerator(channel).text("This command is on cooldown. (Remaining: " + handler.getCooldown("clear") / 1000 + "s)").color(Color.RED).sendTemporary();
			return;
		}
		else if(amount <= 0)
		{
			new EmbedGenerator(channel).text("Invalid amount entered.").color(Color.RED).sendTemporary();
			return;
		}
		else if(amount > 51)
		{
			new EmbedGenerator(channel).text("You tried to delete too many messages (Limit: 50)").color(Color.RED).sendTemporary();
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
                	if(messages.size() <= 1) 
                	{
                		messages.get(0).delete().complete();
                		new EmbedGenerator(channel).text("Deleted " + (messages.size()) + " message").color(Color.GREEN).sendTemporary(5000);
                	}
                	else
                	{
                		channel.deleteMessages(messages).complete();
                		new EmbedGenerator(channel).text("Deleted " + (messages.size()) + " messages").color(Color.GREEN).sendTemporary(5000);
                	}
                }
                catch (Exception exception)
                {
                	new EmbedGenerator(channel).text("Messages in the channel is less than the amount specified.").color(Color.RED).sendTemporary();
                }
	        }
	        )
	        .run();
		}
	}
}
