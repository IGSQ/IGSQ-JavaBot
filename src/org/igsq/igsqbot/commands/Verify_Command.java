package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
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
	private String guessedRoles = "";
	private String guessedAliases = "";
	private String confirmedRoles = "";

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
		//TODO: verifier reacting check
		String messageContent = "";
		String[] retrievedRoles = Common_Command.retrieveRoles(guild.getId());
		String[] assignedRoles = new String[0];
		String[] declinedRoles = new String[0];
		String queryString = "";
		String verificationQueryMessage = "";
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
		else if(Common.getMemberFromUser(toVerify, guild).isOwner())
		{
			new EmbedGenerator(channel).text("You cannot verify the owner.").color(Color.RED).sendTemporary();
			return;
		}
		else
		{
			Role verifiedRole = guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", "guild"));
			if(verifiedRole != null)
			{
				if(Common.getMemberFromUser(toVerify, guild).getRoles().contains(verifiedRole))
				{
					new EmbedGenerator(channel).text("This member is already verified.").color(Color.RED).sendTemporary();
					return;
				}
			}
		}
		
		for(Message selectedMessage : channel.getHistory().retrievePast(10).complete()) 
		{
			if(selectedMessage.getAuthor().equals(toVerify) && !selectedMessage.getAuthor().equals(Common.jda.getSelfUser())) 
			{
				messageContent += " " + selectedMessage.getContentRaw();
			}
		}
		
		int currentRole = 0;
		for(String[] selectedAliases : Common_Command.retrieveAliases(guild.getId()))
		{
			for(String selectedAlias : selectedAliases)
			{
				if(messageContent.contains(selectedAlias))
				{
					verificationQueryMessage += "Detected Alias: " + selectedAlias + " for role <@&" + retrievedRoles[currentRole] + "> (Known)\n";
					assignedRoles = Common.append(assignedRoles, retrievedRoles[currentRole]);
					messageContent = Common.stringDepend(messageContent, selectedAlias);
					confirmedRoles += "," + retrievedRoles[currentRole];
					break;
				}
			}
			currentRole++;
		}
		
		currentRole = 0;
		for(String[] declinedAliases : Common_Command.retrieveDeclined(guild.getId()))
		{
			for(String declinedAlias : declinedAliases)
			{
				if(messageContent.contains(declinedAlias) && !Common.isValueInArray(assignedRoles, retrievedRoles[currentRole]))
				{
					declinedRoles = Common.append(declinedRoles, retrievedRoles[currentRole]);
					break;
				}
			}
			currentRole++;
		}
		
		currentRole = 0;
		String[] wordList = messageContent.split(" ");
		if(assignedRoles.length < 2)
		{
			for(String[] selectedAliases : Common_Command.retrieveAliases(guild.getId()))
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
						
						if(Common.isOption(selectedAlias, queryString, 15) && !guessedRoles.contains(retrievedRoles[currentRole]) && !Common.isValueInArray(declinedRoles, retrievedRoles[currentRole]) && !Common.isValueInArray(assignedRoles, retrievedRoles[currentRole]))
						{
							verificationQueryMessage += "Detected Country: <@&" + retrievedRoles[currentRole] + "> (Guess)\n";
							guessedRoles += "," + retrievedRoles[currentRole];
							guessedAliases += "," + queryString;
							continue;
						}
					}
				}
				currentRole ++;
			}
		}
		
		if(verificationQueryMessage.isEmpty()) verificationQueryMessage = "No roles found";
		EmbedGenerator embed = new EmbedGenerator(channel).title("Roles found for user: " + toVerify.getAsTag()).element("Roles:", verificationQueryMessage).footer("This verification was intitiated by " + author.getAsTag());
		channel.sendMessage(embed.getBuilder().build()).queue
		(
			message ->
			{
				Yaml.updateField(message.getId() + ".verification.enabled", "internal", true);
				Yaml.updateField(message.getId() + ".verification.guessedroles", "internal", this.guessedRoles);
				Yaml.updateField(message.getId() + ".verification.guessedaliases", "internal", this.guessedAliases);
				Yaml.updateField(message.getId() + ".verification.confirmedroles", "internal", this.confirmedRoles);
				Yaml.updateField(message.getId() + ".verification.member", "internal", this.toVerify.getId());
				for(String reaction : Common.QUESTION_REACTIONS) message.addReaction(reaction).queue();
			}
		);
	}
}

