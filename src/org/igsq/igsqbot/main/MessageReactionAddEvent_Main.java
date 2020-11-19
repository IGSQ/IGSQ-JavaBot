package org.igsq.igsqbot.main;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.commands.Common_Command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Main extends ListenerAdapter 
{
	public MessageReactionAddEvent_Main()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		if(!(event.getUser() == null) && !event.getUser().isBot())
		{
			Message message = event.retrieveMessage().complete();
			String messageID = event.getMessageId();
			User user = event.retrieveUser().complete();
			Member member = event.retrieveMember().complete();
			Guild guild = event.getGuild();
			
			if(Yaml.getFieldBool(messageID + ".report.enabled", "internal"))
			{
				Member reportedUser = guild.retrieveMemberById(Yaml.getFieldString(messageID + ".report.reporteduser", "internal")).complete();
				// Member reportingUser = event.getGuild().getMemberById(Yaml.getFieldInt(messageID + ".report.reportinguser", "internal"));
				MessageEmbed embed = message.getEmbeds().get(0);
				if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+2705") && event.getMember().canInteract(reportedUser))
				{
					new EmbedGenerator(event.getChannel(), embed).footer("This was dealt with by " + event.getUser().getAsTag()).color(Color.GREEN).replace(message, true);
					Yaml.updateField(messageID + ".report", "internal", null);
				}
				else
				{
					new EmbedGenerator(event.getChannel(), embed).color(Color.YELLOW).footer(event.getUser().getAsTag() + ", you are not higher than " + reportedUser.getRoles().get(0).getName() + ".").replace(message, 5000, embed);
					event.getReaction().removeReaction(user).queue();
				}
		
			}
			
			else if(Yaml.getFieldBool(messageID + ".help.enabled", "internal"))
			{
				event.getReaction().removeReaction(event.retrieveUser().complete()).queue();
				if(Yaml.getFieldString(messageID + ".help.user","internal").equals(event.getUserId()))
				{
					int page = Yaml.getFieldInt(messageID + ".help.page", "internal");
					if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+25c0"))
					{
						page--;
						if(page == 0) page = Common_Command.PAGE_TEXT.length;
						Yaml.updateField(messageID + ".help.page", "internal", page);
					}
					else if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+25b6")) 
					{
						page++;
						if(page == Common_Command.PAGE_TEXT.length + 1) page = 1;
						Yaml.updateField(messageID + ".help.page", "internal", page);
					}
					EmbedGenerator embed = Common_Command.PAGE_TEXT[page-1];
					embed.setChannel(message.getChannel()).replace(message);
				}
			}
			
			else if(Yaml.getFieldBool(messageID + ".verification.enabled", "internal"))
			{
				String[] guessedRoles = Yaml.getFieldString(messageID + ".verification.guessedroles", "internal").split(",");
				String[] guessedAliases = Yaml.getFieldString(messageID + ".verification.guessedaliases", "internal").split(",");
				String[] confirmedRoles = Yaml.getFieldString(messageID + ".verification.confirmedroles", "internal").split(",");
	
				Member verifiedMember = Common.getMemberFromUser(Common.jda.retrieveUserById(Yaml.getFieldString(messageID + ".verification.member", "internal")).complete(), guild);
				// if(!member.canInteract(verifiedMember)) { event.getReaction().removeReaction(user).queue(); return; }
				
				if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+1f44d"))
				{
					for(int i = 0; i < guessedRoles.length; i++)
					{
						Common_Command.insertAlias(guild.getId(), guessedRoles[i], guessedAliases[i]);
						confirmedRoles = Common.append(confirmedRoles, guessedRoles[i]);
					}
				}
				else if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+1f44e"))
				{
					for(int i = 0; i < guessedRoles.length; i++)
					{
						Common_Command.insertDecline(guild.getId(), guessedRoles[i], guessedAliases[i]);
					}
				}
				if(!Common.isFieldEmpty(guild.getId() + ".welcomechannel", "guild"))
				{
					String parsedRoles = "";
					
					for(String selectedRole : confirmedRoles)  if(!selectedRole.isEmpty()) parsedRoles += " <@&" + selectedRole + "> "; 
					if(parsedRoles.equals("<@&>") || parsedRoles.isEmpty()) parsedRoles = "No roles found.";

					GuildChannel welcomeChannel = Common.jda.getGuildChannelById(Yaml.getFieldString(guild.getId() + ".welcomechannel", "guild"));
					new EmbedGenerator((MessageChannel) welcomeChannel).text(verifiedMember.getAsMention() + ", Welcome to the " + guild.getName() + "!").author(verifiedMember.getUser().getAsTag()).thumbnail(verifiedMember.getUser().getEffectiveAvatarUrl()).element("Roles", parsedRoles).send();
					
					for(String selectedRole : Yaml.getFieldString(guild.getId() + ".serverjoinroles", "guild").split(",")) 
					{
						try
						{
							guild.removeRoleFromMember(verifiedMember, guild.getRoleById(selectedRole)).queue();
						}
						catch(Exception exception) { continue; }
					}
					for(String selectedRole : confirmedRoles)
					{
						try 
						{ 
							guild.addRoleToMember(verifiedMember, guild.getRoleById(selectedRole)).queue(); 
						}
						catch(Exception exception) { }
					}
					try {guild.addRoleToMember(verifiedMember, guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", "guild"))).queue(); }
					catch(Exception exception) { }
					
				}
				message.delete().queue();
			}
		}
    }
}
  


