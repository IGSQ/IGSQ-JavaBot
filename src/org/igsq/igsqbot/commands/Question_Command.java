package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Question_Command {

	private User target;
	private MessageChannel channel;
	private User author;
	private String[] args;

	public Question_Command(MessageReceivedEvent event,String[] args) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		event.getMessage();
		this.args = args;
		
		questionQuery();
	}
	
	private void questionQuery()
	{
		if(!author.isBot()) question();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void question() 
	{
		target = Common.getUserFromMention(args[0]);
		if(target != null) 
		{
			if(target.equals(author)) new EmbedGenerator(channel).text("You cant't ask yourself!").color(Color.RED).sendTemporary();
			else if(args.length <= 1) new EmbedGenerator(channel).text("Please mention a person & write a question.").color(Color.RED).sendTemporary();
			else if(target.isBot()) new EmbedGenerator(channel).text("You may not ask bots.").color(Color.RED).sendTemporary();
			else new EmbedGenerator(channel).author(author.getAsTag()).text(args[1]).thumbnail(target.getAvatarUrl()).footer(author.getAsTag() + "asking " +  target.getAsTag(), author.getAvatarUrl()).color(Color.GREEN).sendQuestion(target,3000);
		}
		else new EmbedGenerator(channel).text("Could not find the mentioned User " + args[0] + ".").color(Color.RED).sendTemporary();
	}
}
