package org.igsq.igsqbot.commands;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.EventWaiter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReactionAddEvent_Verification extends ListenerAdapter 
{
	public MessageReactionAddEvent_Verification()
	{
		Common.jdaBuilder.addEventListeners(this);
	}
	
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		Message message = event.retrieveMessage().complete();
		String messageID = event.getMessageId();
		User user = event.retrieveUser().complete();
		Member member = event.retrieveMember().complete();
		Guild guild = event.getGuild();
		
		if(Yaml.getFieldBool(messageID + ".verification.enabled", "internal") && !user.isBot() && !EventWaiter.waitingOnThis(event))
		{
			String[] guessedRoles = Yaml.getFieldString(messageID + ".verification.guessedroles", "internal").split(",");
			String[] guessedAliases = Yaml.getFieldString(messageID + ".verification.guessedaliases", "internal").split(",");
			String[] confirmedRoles = Yaml.getFieldString(messageID + ".verification.confirmedroles", "internal").split(",");

			Member verifiedMember = Common.getMemberFromUser(Common.jda.retrieveUserById(Yaml.getFieldString(messageID + ".verification.member", "internal")).complete(), guild);
			Member initiater = Common.getMemberFromUser(Common.jda.retrieveUserById(Yaml.getFieldString(messageID + ".verification.verifier", "internal")).complete(), guild);
			// if(!member.canInteract(verifiedMember)) { event.getReaction().removeReaction(user).queue(); return; }
			if(!initiater.equals(member)) 
			{ 
				try
				{
					event.getReaction().removeReaction(user).queue(); 
				}
				catch(Exception exception) { }
				return; 
			}
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
