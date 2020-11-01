package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Messaging;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Match_Command {

	private TextChannel channel;
	private User author;
	private Message message;
	private String[] args;

	public Match_Command(MessageReceivedEvent event, String[] args) 
	{
		this.author = event.getAuthor();
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		this.args = args;
		
		matchQuery();
	}

	private void matchQuery()
	{
		if(!author.isBot() && message.isFromType(ChannelType.TEXT)) match();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
		
	}

	private void match()
	{
		if(args.length == 3) 
		{
			Messaging.embed(channel).text(Common.isOption(args[0], args[1], Double.parseDouble(args[2]))? "True" : "False").color(Color.YELLOW).sendTemporary();
		}
		else new EmbedGenerator(channel).text("A match test needs 3 args!").color(Color.RED).sendTemporary();
	}
}
