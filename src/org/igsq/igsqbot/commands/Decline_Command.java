package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Decline_Command 
{
	private User author;
	private MessageChannel channel;
	private Message message;
	private Guild guild;
	private String[] args;
	private String action;
	private String alias;
	private Role role;

	public Decline_Command(MessageReceivedEvent event, String[] args) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.message = event.getMessage();
		this.args = args;
		this.guild = event.getGuild();
		if(!channel.getType().equals(ChannelType.TEXT))  new EmbedGenerator(channel).text("This command can only be done in a guild.").color(Color.RED).sendTemporary();
	
		aliasQuery();
	}
	
	private void aliasQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && !author.isBot()) alias();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void alias()
	{
		try { action = args[0]; }
		catch(Exception exception) { new EmbedGenerator(channel).text("You entered an invalid action").send(); return; }
		
		try{ role = Common.getRoleFromMention(guild, args[1]); }
		catch(Exception exception) { role = null; }
		
		if(role == null)
		{
			new EmbedGenerator(channel).text("Mention a role to alias.").color(Color.RED).sendTemporary();
			return;
		}
		
		try { alias = args[2]; }
		catch(Exception exception) { new EmbedGenerator(channel).text("You entered an invalid alias").send(); return; }
		
		switch(action.toLowerCase())
		{
			case "add":
			case "accept":
			case "yes":
				Common_Command.insertDecline(guild.getId(), role.getId(), alias);
				new EmbedGenerator(channel).text("Added decline: " + alias + " for role: " + role.getAsMention()).sendTemporary();
				break;
			
			default:
				new EmbedGenerator(channel).text("You entered an invalid action").send(); return; 
		}
		
	}
}
