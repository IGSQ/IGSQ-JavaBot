package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Template_Command 
{
	private TextChannel channel;
	private User author;
	private Member guildAuthor;
	private Message message;
	
	public Template_Command(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		this.guildAuthor = event.getMember();
		
		pollQuery();
	}
	private void pollQuery()
	{
	
	}

	private void poll()
	{
		
	}
}
