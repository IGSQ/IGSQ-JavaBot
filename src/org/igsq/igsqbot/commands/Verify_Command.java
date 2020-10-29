package org.igsq.igsqbot.commands;

import java.awt.Color;
import java.util.Locale;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
@SuppressWarnings("unused")
public class Verify_Command 
{
	private Member me;
	private Member guildAuthor;
	private TextChannel channel;
	private User author;
	private Message message;
	private Guild guild;
	
	private User toVerify;
	private String[] foundCountries = {};
	private Message[] messagesFromUser = {};

	public Verify_Command(MessageReceivedEvent event) 
	{
		this.me = event.getGuild().getSelfMember();
		this.author = event.getAuthor();
		this.guildAuthor = event.getMember();
		this.channel = event.getTextChannel();
		this.message = event.getMessage();
		this.guild = event.getGuild();
		
		verifyQuery();
	}
	
	private void verifyQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && !author.isBot() && !channel.getId().equalsIgnoreCase(Yaml.getFieldString(guild.getId() + ".verificationchannel", "guild"))) verify();
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
				String[] wordsInMessage = selectedMessage.getContentRaw().split(" ");
				
				for(int i = 0; i < wordsInMessage.length; i++)
				{
					for(String selectedLocaleCode : Locale.getISOCountries())
					{
						Locale locale = new Locale("en", selectedLocaleCode);
						String country = locale.getDisplayCountry();
						String wordToQuery = "";
						
						for(String selectedAlias : Common_Command.VERIFICATION_ALIASES)
						{
							if(Common.areStringsCloseMatch(wordsInMessage[i], selectedAlias, 70)) 
							{
								wordToQuery = wordsInMessage[i] + " " + wordsInMessage[i + 1];
								break;
							}
								
						}
						if(wordToQuery.isEmpty()) wordToQuery = wordsInMessage[i];
						
						if(Common.areStringsCloseMatch(wordToQuery.toLowerCase(), country.toLowerCase(), 70))
						{
							System.out.println("WORD MATCH: " + wordToQuery + " LOCALE: " + country);
						}
					}	
				}
			}
		}
	
		for(String selectedCountry : foundCountries)
		{
			channel.sendMessage(selectedCountry);
		}
	}		
}

