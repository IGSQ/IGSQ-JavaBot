package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.Yaml;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Modhelp_Command {
	private final MessageChannel channel;
	private final User author;
	private final Member member;

	public Modhelp_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.member = event.getMember();
		modhelpQuery();
	}

	private void modhelpQuery()
	{
		if(!author.isBot() && member.hasPermission(Permission.MESSAGE_MANAGE)) modhelp();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}

	private void modhelp()
	{
		EmbedGenerator embed = Common_Command.HELP_PAGE_TEXT[0];
		embed.setChannel(this.channel);
		channel.sendMessage(embed.getBuilder().build()).queue
		(
				message ->
				{
					Yaml.updateField(message.getId() + ".modhelp.user", "internal", author.getId());
					Yaml.updateField(message.getId() + ".modhelp.enabled", "internal", true);
					Yaml.updateField(message.getId() + ".modhelp.page", "internal", 1);
					message.addReaction("U+25C0").queue();	
					message.addReaction("U+25B6").queue();		
				}
		);
	}
}