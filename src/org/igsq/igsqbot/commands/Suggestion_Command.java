package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Suggestion_Command 
{
    private String[] args;
    private Message message;
    private User author;
    private TextChannel channel;
    private MessageChannel suggestionChannel;
    private Guild guild;
    private JDA jda;

    public Suggestion_Command(MessageReceivedEvent event)
    {
        this.args = event.getMessage().getContentRaw().toLowerCase().split(" ", 2);
        this.args = Common.depend(args, 0);
        this.message = event.getMessage();
        this.author = event.getAuthor();
        this.jda = event.getJDA();
        if(event.getChannelType().equals(ChannelType.TEXT)) 
        {
            this.channel = event.getTextChannel();
            this.guild = event.getGuild();
            suggestQuery();
        }
        else
        {
            new EmbedGenerator(event.getChannel()).text("This command can only be done in a guild.").color(Color.RED).sendTemporary();
        }

    }
    private void suggestQuery()
	{
		if(!author.isBot() && message.isFromType(ChannelType.TEXT)) suggest();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();	
	}
    private void suggest()
    {
    	suggestionChannel = jda.getTextChannelById(Yaml.getFieldString(guild.getId() + ".suggestionchannel", "guild"));
    	new EmbedGenerator(suggestionChannel).title("Suggestion:").text(args[0]).thumbnail(author.getAvatarUrl()).footer("Suggestion by: " + Common.getMemberFromUser(author, guild).getNickname()).send();
    }
}