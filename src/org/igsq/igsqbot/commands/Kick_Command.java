package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.EmbedGenerator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Kick_Command {

	private Member me;
	private Member guildAuthor;
	private TextChannel channel;
	private User author;
	private Message message;
	private Guild guild;
	private String result = "";

	public Kick_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.message = event.getMessage();
		if(event.getChannelType().equals(ChannelType.TEXT)) 
		{
			this.channel = event.getTextChannel();
			this.guildAuthor = event.getMember();
			this.me = event.getGuild().getSelfMember();
			this.guild = event.getGuild();
			kickQuery();
		}
		else
		{
			new EmbedGenerator(event.getChannel()).text("This command can only be done in a guild.").color(Color.RED).sendTemporary();
		}
		
	}
	
	private void kickQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && me.hasPermission(Permission.KICK_MEMBERS) && !author.isBot() && guildAuthor.hasPermission(Permission.KICK_MEMBERS)) kick();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void kick() {
		if(!message.getMentionedMembers(guild).isEmpty())
		{
			for(Member selectedMember : message.getMentionedMembers(guild))
			{
				if(me.canInteract(selectedMember) && guildAuthor.canInteract(selectedMember)) 
				{
					guild.kick(selectedMember).queue();
					result += "Kicked member " + selectedMember.getAsMention() + ". :white_check_mark:\n";
				}
				else result += selectedMember.getAsMention() +" has higher roles. :crown:\n";
			}
			new EmbedGenerator(channel).text(result).color(Color.CYAN).sendTemporary(5000);
		}
		else new EmbedGenerator(channel).text("Please specify at least 1 user to kick! :warning:").color(Color.RED).sendTemporary();
	}
}
