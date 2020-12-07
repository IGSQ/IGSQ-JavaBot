package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.Random;

import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Shutdown_Command {

	private final MessageChannel channel;
	private final User author;
	private final Random random = new Random();
	private final JDA jda;

	public Shutdown_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.jda = event.getJDA();
		shutdownQuery();
	}
	
	private void shutdownQuery()
	{
		if(!author.isBot()) shutdown();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void shutdown() 
	{
		new EmbedGenerator(channel).text(Common_Command.SHUTDOWN_MESSAGES[random.nextInt(Common_Command.SHUTDOWN_MESSAGES.length)]).color(Color.GRAY).send();
		jda.shutdown();
		Yaml.saveFileChanges("@all");
		Yaml.disregardAndCloseFile("@all");
		System.exit(0);
		
	}
}
