package org.igsq.igsqbot.deprecated;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.*;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Verify_Command 
{
	private final JDA jda;
	private final Message message;
	private final User author;

	private TextChannel channel;
	private Guild guild;
	private String guessedRoles = "";
	private String guessedAliases = "";
	private String confirmedRoles = "";
	private User toVerify;

	public Verify_Command(MessageReceivedEvent event) 
	{
		this.author = event.getAuthor();
		this.message = event.getMessage();
		this.jda = event.getJDA();
		
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
		List<Message> retrievedMessages = getMessages(channel, 10);
		String messageContent = "";
		String[] retrievedRoles = Command_Utils.getRoles(guild.getId());
		String[] assignedRoles = new String[0];
		String[] declinedRoles = new String[0];
		String queryString = "";
		String verificationMessage = "";

		try
		{
			toVerify = message.getMentionedUsers().get(0);
		}
		
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("Mention someone to verify.").color(Color.RED).sendTemporary();
			return;
		}
		
		if(!channel.getId().equalsIgnoreCase(Yaml.getFieldString(guild.getId() + ".verificationchannel", "guild")))
		{
			new EmbedGenerator(channel).text("This is not the setup verification channel.").color(Color.RED).sendTemporary();
			return;
		}
		else if(toVerify.isBot())
		{
			new EmbedGenerator(channel).text("You cannot verify bots.").color(Color.RED).sendTemporary();
			return;
		}
		else if(User_Utils.getMemberFromUser(toVerify, guild).isOwner())
		{
			new EmbedGenerator(channel).text("You cannot verify the owner.").color(Color.RED).sendTemporary();
			return;
		}
		else
		{
			if(!Yaml_Utils.isFieldEmpty(guild.getId() + ".verifiedrole", "guild"))
			{
				Role verifiedRole = guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", "guild"));

				if(verifiedRole != null && User_Utils.getMemberFromUser(toVerify, guild).getRoles().contains(verifiedRole))
				{
					new EmbedGenerator(channel).text("This member is already verified.").color(Color.RED).sendTemporary();
					return;
				}
				else
				{
					new EmbedGenerator(channel).text("There is no verified role setup.").color(Color.RED).sendTemporary();
					return;
				}
			}
		}

		if(retrievedMessages != null)
		{
			for(Message selectedMessage : retrievedMessages)
			{
				if(selectedMessage.getAuthor().equals(toVerify) && !selectedMessage.getAuthor().equals(jda.getSelfUser()))
				{
					messageContent += "" + selectedMessage.getContentRaw();
				}
			}
		}
		else
		{
			new EmbedGenerator(channel).text("An error occurred while retrieving the users messages.").color(Color.RED).sendTemporary();
			return;
		}



		int currentRole = 0;
		for(String[] selectedAliases : Command_Utils.getAliases(guild.getId()))
		{
			for(String selectedAlias : selectedAliases)
			{
				if(messageContent.contains(selectedAlias))
				{
					verificationMessage += "Detected Alias: " + selectedAlias + " for role <@&" + retrievedRoles[currentRole] + "> (Known)\n";
					assignedRoles = Array_Utils.append(assignedRoles, retrievedRoles[currentRole]);
					messageContent = String_Utils.stringDepend(messageContent, selectedAlias);
					confirmedRoles += "," + retrievedRoles[currentRole];
					break;
				}
			}
			currentRole++;
		}
		
		currentRole = 0;
		for(String[] declinedAliases : Command_Utils.getDeclined(guild.getId()))
		{
			for(String declinedAlias : declinedAliases)
			{
				if(messageContent.contains(declinedAlias) && !Array_Utils.isValueInArray(assignedRoles, retrievedRoles[currentRole]))
				{
					declinedRoles = Array_Utils.append(declinedRoles, retrievedRoles[currentRole]);
					break;
				}
			}
			currentRole++;
		}
		
		currentRole = 0;
		String[] wordList = messageContent.split(" ");
		if(assignedRoles.length < 2)
		{
			for(String[] selectedAliases : Command_Utils.getAliases(guild.getId()))
			{
				for(String selectedAlias: selectedAliases)
				{
					for(int i = 0; i < wordList.length; i++)
					{
						queryString = "";
						try
						{
							queryString = wordList[i] + " " + wordList[i + 1];
						}
						catch(Exception exception)
						{
							queryString = wordList[i];
						}
						
						if(String_Utils.isOption(selectedAlias, queryString, 15) && !guessedRoles.contains(retrievedRoles[currentRole]) && !Array_Utils.isValueInArray(declinedRoles, retrievedRoles[currentRole]) && !Array_Utils.isValueInArray(assignedRoles, retrievedRoles[currentRole]))
						{
							verificationMessage += "Detected Country: <@&" + retrievedRoles[currentRole] + "> (Guess)\n";
							guessedRoles += "," + retrievedRoles[currentRole];
							guessedAliases += "," + queryString;
						}
					}
				}
				currentRole ++;
			}
		}
		
		if(verificationMessage.isEmpty()) verificationMessage = "No roles found";
		EmbedGenerator embed = new EmbedGenerator(channel).title("Roles found for user: " + toVerify.getAsTag()).element("Roles:", verificationMessage).footer("This verification was intitiated by " + author.getAsTag());
		channel.sendMessage(embed.getBuilder().build()).queue
		(
			restMessage ->
			{
				Yaml.updateField(message.getId() + ".verification.enabled", "internal", true);
				Yaml.updateField(message.getId() + ".verification.guessedroles", "internal", this.guessedRoles);
				Yaml.updateField(message.getId() + ".verification.guessedaliases", "internal", this.guessedAliases);
				Yaml.updateField(message.getId() + ".verification.confirmedroles", "internal", this.confirmedRoles);
				Yaml.updateField(message.getId() + ".verification.member", "internal", this.toVerify.getId());
				Yaml.updateField(message.getId() + ".verification.verifier", "internal", this.author.getId());
				for(String reaction : Common.THUMB_REACTIONS) message.addReaction(reaction).queue();
			}
		);
	}
	
	private List<Message> getMessages(TextChannel channel, int amount)
	{
		try
		{
			return channel.getHistory().retrievePast(amount).submit().get();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			return Collections.emptyList();
		}
	}

}

