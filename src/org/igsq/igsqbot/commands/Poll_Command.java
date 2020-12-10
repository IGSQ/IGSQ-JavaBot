package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.Messaging;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Poll_Command {

	private final MessageChannel channel;
	private final User author;
	private final String[] args;
	private String options = "";
	private String[] reactions = {};
    private Color color = Color.LIGHT_GRAY;

	public Poll_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		if(event.getChannelType().equals(ChannelType.TEXT)) this.color = event.getMember().getColor();
		this.args = Common.removeBeforeCharacter(event.getMessage().getContentRaw(), ' ').split("/");
		
		pollQuery();
	}

	private void pollQuery()
	{
		if(!author.isBot()) poll();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
		
	}

	private void poll()
	{
		if(args.length >= 3) 
		{
            String topic = args[0];
			for(int i=1; i < args.length && i < Messaging.REACTION_LIMIT+1; i++)
			{
				options += args[i] +" " + Common_Command.POLL_EMOJIS[i-1]  + "\n";
				if(args.length <= Messaging.REACTION_LIMIT/2+1) options += "\n";
				reactions = Common.append(reactions, Common_Command.POLL_EMOJIS_UNICODE[i-1]);
			}
			new EmbedGenerator(channel).title("Poll:").text(topic).element("Options:", options).footer("Poll created by "+ author.getAsTag()).thumbnail(author.getAvatarUrl()).color(color).reaction(reactions).send();
		}
		else new EmbedGenerator(channel).text("A poll needs at least 2 options!").color(Color.RED).sendTemporary();
	}
}
