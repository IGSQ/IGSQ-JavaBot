package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.Locale;

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
	private String countryString = "";
	private String gameString = "";

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
			new EmbedGenerator(event.getChannel()).text("This Command Can Only be done in a guild.").color(Color.RED).sendTemporary();
		}
		
	}
	
	private void verifyQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && !author.isBot()) verify();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void verify() 
	{
		if(!channel.getId().equalsIgnoreCase(Yaml.getFieldString(guild.getId() + ".verificationchannel", "guild")))
		{
			new EmbedGenerator(channel).text("This is not the setup verification channel.").color(Color.RED).sendTemporary();
			return;
		}
		
		try
		{
			toVerify = message.getMentionedUsers().get(0);
		}
		catch(IndexOutOfBoundsException exception)
		{
			new EmbedGenerator(channel).text("Mention someone to verify.").color(Color.RED).sendTemporary();
			return;
		}
		
		for(Message selectedMessage : channel.getHistory().retrievePast(10).complete()) 
		{
			if(selectedMessage.getAuthor().equals(toVerify) && !selectedMessage.getAuthor().equals(Common.jda.getSelfUser())) 
			{
				String[] words = selectedMessage.getContentRaw().split(" ");
				String queryString = "";
				
				for(int i = 0; i < words.length; i++)
				{
					queryString = words[i];
					for(String selectedPrefix : Common.PREFIXES)
					{
						if(Common.isOption(selectedPrefix, words[i], 30))
						{
							try {queryString = words[i] + " " + words[i+1]; i++;} catch(Exception exception) {}
							break;
						}
					}
					performQuery(queryString, guild.getId());
				}
			}
		}

		if(countryString.isEmpty()) countryString = "No countries found";
		if(gameString.isEmpty()) gameString = "No games found.";
		new EmbedGenerator(channel).title("Roles found for user: " + toVerify.getAsTag()).element("Countries:", countryString).element("Games:", gameString).reaction(Common.QUESTION_REACTIONS).footer("This verification was intitiated by " + author.getAsTag()).sendTemporary();;
	}
	
	private boolean isCountry(String arg)
	{
		for(String selectedLocaleCode : Locale.getISOCountries())
		{
			Locale locale = new Locale("en", selectedLocaleCode);
			String selectedCountry = locale.getDisplayCountry();
			
			if(arg.equalsIgnoreCase(selectedCountry)) return true;
		}
		return false;
	}
	
	private void performQuery(String query, String id)
	{
		// Check all known words for the query, only continue if it doesnt exist
		String currentName;
		for(int i = 0; i < Yaml.getFieldInt(id + ".references.referencecount", "verification"); i++) 
		{
			for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".aliases", "verification").split(","))
			{
				if(selectedAlias.equalsIgnoreCase(query))
				{
					currentName = Yaml.getFieldString(id + ".references." + i + ".name", "verification");
					
					if(isCountry(currentName)) countryString += currentName + " (Known)" + "\n"; // this will resolve roles rather than names.
					else gameString += currentName + " (Known)" + "\n"; // this will resolve roles rather than names.
					
					return;
				}
			}
		}

		// No country was found on file (therefor we must search for it)
		for(String selectedLocaleCode : Locale.getISOCountries())
		{
			Locale locale = new Locale("en", selectedLocaleCode);
			String selectedCountry = locale.getDisplayCountry();
			
			if(Common.isOption(selectedCountry, query, 10))
			{
				// If a close match is found, add it to the suggested section.
				Yaml.updateField(id + "." + query + ".suggested", "verification", query);
				countryString += selectedCountry + " (Suggested)" +"\n";
				return;
			}
		}
		

		for(String selectedGame : Common.GAMES)
		{
			if(Common.isOption(selectedGame, query, 10))
			{
				Yaml.updateField(id + "." + query + ".suggested", "verification", query);
				gameString += selectedGame + " (Suggested)" + "\n";
				return;
			}
		}	
	}
}

