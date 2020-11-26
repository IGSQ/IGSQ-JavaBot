package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Modhelp_Command {
	private MessageChannel channel;
	private User author;

	public Modhelp_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		modHelpQuery();
	}

	private void modHelpQuery()
	{
		if(!author.isBot()) modhelp();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}

	private void modhelp()
	{
		EmbedGenerator embed = Common_Command.PAGE_TEXT[0];
		embed.setChannel(this.channel);
		channel.sendMessage(embed.getBuilder().build()).queue
		(
				message ->
				{
					Yaml.updateField(message.getId() + ".help.user", "internal", author.getId());
					Yaml.updateField(message.getId() + ".help.enabled", "internal", true);
					Yaml.updateField(message.getId() + ".help.page", "internal", 1);
					message.addReaction("U+25C0").queue();	
					message.addReaction("U+25B6").queue();		
				}
		);
	}
}