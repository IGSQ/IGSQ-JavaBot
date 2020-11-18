package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Verify_Command 
{
	private TextChannel channel;
	private User author;
	private Message message;
	private Guild guild;
	private User toVerify;
	private String roleString = "";

	public Verify_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.message = event.getMessage();
		
		if(event.getChannelType().equals(ChannelType.TEXT)) 
		{
			this.channel = event.getTextChannel();
			this.guild = event.getGuild();
			verifyQuery();
		}
		else
		{
			new EmbedGenerator(event.getChannel()).text("This command can only be done in a guild.").color(Color.RED).sendTemporary();
		}
		
	}
	
	private void verifyQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && !author.isBot()) verify();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void verify() 
	{
		String messageContent = "";
		String[] retrievedRoles = Common_Command.retrieveRoles(guild.getId());
		String[] assignedRoles = new String[0];
		
		if(!channel.getId().equalsIgnoreCase(Yaml.getFieldString(guild.getId() + ".verificationchannel", "guild")))
		{
			new EmbedGenerator(channel).text("This is not the setup verification channel.").color(Color.RED).sendTemporary();
			return;
		}
		
		try
		{
			toVerify = message.getMentionedUsers().get(0);
		}
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("Mention someone to verify.").color(Color.RED).sendTemporary();
			return;
		}
		
		for(Message selectedMessage : channel.getHistory().retrievePast(10).complete()) 
		{
			if(selectedMessage.getAuthor().equals(toVerify) && !selectedMessage.getAuthor().equals(Common.jda.getSelfUser())) 
			{
				messageContent += " " + selectedMessage.getContentRaw();
			}
		}
		int i = 0;
		for(String[] selectedAliases : Common_Command.retrieveAliases(guild.getId()))
		{
			for(String selectedAlias : selectedAliases)
			{
				if(messageContent.indexOf(selectedAlias) >= 0)
				{
					roleString += "Detected Alias: " + selectedAlias + " for role <@&" + retrievedRoles[i] + ">\n";
					assignedRoles = Common.append(assignedRoles, retrievedRoles[i]);
					//TODO: subtract selectedAlias from messageContent
					break;
				}
			}
			i++;
		}
		
		i = 0;
		for(String[] declinedAliases : Common_Command.retrievedDeclined(guild.getId()))
		{
			for(String declinedAlias : declinedAliases)
			{
				for(String selectedRole : assignedRoles)
				{
					if(messageContent.contains(declinedAlias) && !selectedRole.equals(retrievedRoles[i]))
					{
						roleString += "Ignored Alias: " + declinedAlias + " for role <@&" + retrievedRoles[i] + ">\n";
						break;
					}
				}
			}
			i++;
		}
		if(roleString.isEmpty()) roleString = "No roles found";
		new EmbedGenerator(channel).title("Roles found for user: " + toVerify.getAsTag()).element("Roles:", roleString).reaction(Common.QUESTION_REACTIONS).footer("This verification was intitiated by " + author.getAsTag()).sendTemporary();
	}
}

