package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.Yaml;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help_Command {
	private final MessageChannel channel;
	private final User author;

	public Help_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		helpQuery();
	}

	private void helpQuery()
	{
		if(!author.isBot()) help();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}

	private void help()
	{
		EmbedGenerator embed = Common_Command.HELP_PAGE_TEXT[0];
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
