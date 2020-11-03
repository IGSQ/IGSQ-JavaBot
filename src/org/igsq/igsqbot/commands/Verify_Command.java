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
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		this.guild = event.getGuild();
		
		verifyQuery();
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
					performQuery(queryString, toVerify.getId());
				}
			}
		}

		if(countryString.isEmpty()) countryString = "No countries found";
		if(gameString.isEmpty()) gameString = "No games found.";
		new EmbedGenerator(channel).title("Roles found for user: " + toVerify.getAsTag()).element("Countries:", countryString).element("Games:", gameString).reaction(Common.QUESTION_REACTIONS).footer("This verification was intitiated by " + author.getAsTag()).sendTemporary();;
		Yaml.updateField(toVerify.getId() + ".verification", "internal", null);
	}
	
	private void performQuery(String query, String id)
	{
		if(Yaml.getFieldString(id + ".verification." + query, "internal") == null || Yaml.getFieldString(id + ".verification." + query, "internal").isEmpty()) // Only continue if the field is empty.
		{
			if(Yaml.getFieldString("countries." + query, "lookup") == null)
			{
				for(String selectedLocaleCode : Locale.getISOCountries())
				{
					Locale locale = new Locale("en", selectedLocaleCode);
					String selectedCountry = locale.getDisplayCountry();
					
					if(Common.isOption(selectedCountry, query, 10))
					{;
						Yaml.updateField(id + ".verification." + query, "internal", "checked");
						Yaml.updateField("countries." + query, "lookup", selectedCountry);
						countryString += selectedCountry + "\n";
						return;
					}
				}
			}
			else
			{
				countryString += Yaml.getFieldString("countries." + query, "lookup") + "\n";
			}
			
			if(Yaml.getFieldString("games." + query, "lookup") == null)
			{
				for(String selectedGame : Common.GAMES)
				{
					if(Common.isOption(selectedGame, query, 10))
					{
						Yaml.updateField(id + ".verification." + query, "internal", "checked");
						Yaml.updateField("games." + query, "lookup", selectedGame);
						gameString += selectedGame + "\n";
						return;
					}
				}	
			}
			else
			{
				gameString += Yaml.getFieldString("games." + query, "lookup") + "\n";
			}
			Yaml.updateField(id + ".verification" + query, "internal", "checked");
		}
	}
}

