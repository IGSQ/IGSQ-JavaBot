package org.igsq.igsqbot.deprecated;

import org.igsq.igsqbot.util.Array_Utils;
import org.igsq.igsqbot.util.Command_Utils;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.util.User_Utils;
import org.igsq.igsqbot.util.Yaml_Utils;

public class MessageReactionAddEvent_Verification extends ListenerAdapter 
{
	@Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {
		Message message = null;
		User user = null;
		Member member = null;
		
		String messageID = event.getMessageId();
		Guild guild = event.getGuild();
		JDA jda = event.getJDA();
		
		try 
		{
			message = event.retrieveMessage().submit().get();
			user = event.retrieveUser().submit().get();
			member = event.retrieveMember().complete();
		} 
		catch (Exception exception) 
		{
			new ErrorHandler(exception);
			return;
		}

		
		if(Yaml.getFieldBool(messageID + ".verification.enabled", "internal") && !user.isBot())
		{
			String[] guessedRoles = Yaml.getFieldString(messageID + ".verification.guessedroles", "internal").split(",");
			String[] guessedAliases = Yaml.getFieldString(messageID + ".verification.guessedaliases", "internal").split(",");
			String[] confirmedRoles = Yaml.getFieldString(messageID + ".verification.confirmedroles", "internal").split(",");

			Member verifiedMember = User_Utils.getMemberFromUser(jda.retrieveUserById(Yaml.getFieldString(messageID + ".verification.member", "internal")).complete(), guild);
			Member initiater = User_Utils.getMemberFromUser(jda.retrieveUserById(Yaml.getFieldString(messageID + ".verification.verifier", "internal")).complete(), guild);
			// if(!member.canInteract(verifiedMember)) { event.getReaction().removeReaction(user).queue(); return; }
			if(!initiater.equals(member)) 
			{ 
				try
				{
					event.getReaction().removeReaction(user).queue(); 
				}
				catch(Exception exception)
				{
					new ErrorHandler(exception);
					return;
				}

			}
			if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+1f44d")) 
			{
				for(int i = 0; i < guessedRoles.length; i++)
				{
					Command_Utils.insertAlias(guild.getId(), guessedRoles[i], guessedAliases[i]);
					confirmedRoles = Array_Utils.append(confirmedRoles, guessedRoles[i]);
				}
			}
			else if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+1f44e"))
			{
				for(int i = 0; i < guessedRoles.length; i++)
				{
					Command_Utils.insertDecline(guild.getId(), guessedRoles[i], guessedAliases[i]);
				}
			}
			if(!Yaml_Utils.isFieldEmpty(guild.getId() + ".welcomechannel", "guild"))
			{
				String parsedRoles = "";
				
				for(String selectedRole : confirmedRoles)  if(!selectedRole.isEmpty()) parsedRoles += " <@&" + selectedRole + "> "; 
				if(parsedRoles.equals("<@&>") || parsedRoles.isEmpty()) parsedRoles = "No roles found.";

				GuildChannel welcomeChannel = jda.getGuildChannelById(Yaml.getFieldString(guild.getId() + ".welcomechannel", "guild"));
				new EmbedGenerator((MessageChannel) welcomeChannel).text(verifiedMember.getAsMention() + ", Welcome to the " + guild.getName() + "!").author(verifiedMember.getUser().getAsTag()).thumbnail(verifiedMember.getUser().getEffectiveAvatarUrl()).element("Roles", parsedRoles).send();
				
				for(String selectedRole : Yaml.getFieldString(guild.getId() + ".serverjoinroles", "guild").split(",")) 
				{
					try
					{
						guild.removeRoleFromMember(verifiedMember, guild.getRoleById(selectedRole)).queue();
					}
					catch(Exception exception)
					{
						new ErrorHandler(exception);
						return;
					}
				}
				for(String selectedRole : confirmedRoles)
				{
					try 
					{ 
						guild.addRoleToMember(verifiedMember, guild.getRoleById(selectedRole)).queue(); 
					}
					catch(Exception exception)
					{
						new ErrorHandler(exception);
						return;
					}
				}
				try {guild.addRoleToMember(verifiedMember, guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", "guild"))).queue(); }
				catch(Exception exception)
				{
					new ErrorHandler(exception);
					return;
				}
				
			}
			message.delete().queue();
		}
    }
}
