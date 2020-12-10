package org.igsq.igsqbot.setup;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.GUIGenerator;

import java.awt.*;

public class Verification_Setup
{
	private final Member member;
	private final User author;
	private final MessageChannel channel;

	public Verification_Setup(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.member = event.getMember();
		this.channel = event.getChannel();
		verificationSetupQuery();
	}

	private void verificationSetupQuery()
	{
		if(!author.isBot() && member.hasPermission(Permission.MESSAGE_MANAGE)) verificationSetup();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}

	private void verificationSetup()
	{
		EmbedGenerator embed = new EmbedGenerator(channel).text(":one: Show current aliases \n :two: Add a new alias \n :three: Quit");
		GUIGenerator gui = new GUIGenerator(embed);
		int menuItem = gui.menu(author, 10000, 3);
		MessageReactionAddEvent event = (MessageReactionAddEvent) gui.getEvent();
		
		switch (menuItem)
		{
			case 1:
				break;
			case 2:
				break;
			case 3:
				gui.getMessage().delete().queue();
				break;
			default:
				if (event == null)
				{
					//Timeout
				}
				else
				{
					//Invalid emoji
					event.getReaction().removeReaction(author).queue();
				}
		}
	}
}

