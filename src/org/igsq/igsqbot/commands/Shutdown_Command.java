package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.Random;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Shutdown_Command {

	private Member guildAuthor;
	private TextChannel channel;
	private User author;
	private Message message;
	private Random random = new Random();

	public Shutdown_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.guildAuthor = event.getMember();
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		
		shutdownQuery();
	}
	
	private void shutdownQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && !author.isBot() && guildAuthor.hasPermission(Permission.ADMINISTRATOR)) shutdown();
		else Common.sendEmbed("You cannot Execute this command!\nThis may be due to being in the wrong channel or not having the required permission.",channel,Color.RED);
	}
	
	private void shutdown() 
	{
		Common.sendEmbed(Common_Command.SHUTDOWN_MESSAGES[random.nextInt(Common_Command.SHUTDOWN_MESSAGES.length)], channel,Color.GRAY);
		Yaml.saveFileChanges("@all");
		Common.jda.shutdown();
	}
}
