package org.igsq.igsqbot.commands.moderation;

import java.awt.Color;

import org.igsq.igsqbot.Common;

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
		this.me = event.getGuild().getSelfMember();
		this.author = event.getAuthor();
		this.guildAuthor = event.getMember();
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		this.guild = event.getGuild();
		
		kickQuery();
	}
	
	private void kickQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && me.hasPermission(Permission.KICK_MEMBERS) && !author.isBot() && guildAuthor.hasPermission(Permission.KICK_MEMBERS)) kick();
		else Common.sendEmbed("You cannot Execute this command!\nThis may be due to being in the wrong channel or not having the required permission.",channel,Color.RED);
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
			Common.sendEmbed(result, channel, Color.CYAN);
		}
		else Common.sendEmbed("Please specify at least 1 user to kick! :warning:", channel,Color.RED);
	}
}
