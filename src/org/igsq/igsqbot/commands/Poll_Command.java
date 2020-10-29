package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Messaging;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Poll_Command {

	private TextChannel channel;
	private User author;
	private Member guildAuthor;
	private Message message;
	private String[] args;
	private String options = "";
	private String[] reactions = {};
	private String topic = "";

	public Poll_Command(MessageReceivedEvent event, String[] args) 
	{
		this.author = event.getAuthor();
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		this.guildAuthor = event.getMember();
		this.args = args;
		
		pollQuery();
	}

	private void pollQuery()
	{
		if(!author.isBot() && message.isFromType(ChannelType.TEXT)) poll();
		else Messaging.sendEmbed("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.",channel,Color.RED);
	}

	private void poll()
	{
		if(args.length >= 3) 
		{
			topic = args[0];
			for(int i=1; i < args.length && i < Common.REACTION_LIMIT+1; i++)
			{
				options += args[i] +" " + Common_Command.POLL_EMOJIS[i-1]  + "\n";
				if(args.length <= Common.REACTION_LIMIT/2+1) options += "\n";
				reactions = Common.append(reactions, Common_Command.POLL_EMOJIS_UNICODE[i-1]);
			}
			Common_Command.sendPoll(new Field[] {new Field("Options:",options,false)}, channel,"Poll:","Poll created by "+ author.getAsTag(),topic,author.getAvatarUrl(),reactions,guildAuthor.getColor());
		}
		else Messaging.sendEmbed("A poll needs at least 2 options!", channel,Color.RED);
	}
}
