package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.util.Command_Utils;

public class Decline_Command 
{
	private final User author;
	private final MessageChannel channel;
	private final Message message;
	private final Guild guild;
	private String[] args;

    public Decline_Command(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.message = event.getMessage();
		this.args = event.getMessage().getContentRaw().toLowerCase().split(" ", 4);
		this.guild = event.getGuild();
		if(!channel.getType().equals(ChannelType.TEXT)) 
		{
			new EmbedGenerator(channel).text("This command can only be done in a guild.").color(Color.RED).sendTemporary();
			return;
		}
	
		aliasQuery();
	}
	
	private void aliasQuery()
	{
		if(message.isFromType(ChannelType.TEXT) && !author.isBot()) alias();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void alias()
	{
		args = Common.depend(args, 0);
        String action;
        try { action = args[0]; }
		catch(Exception exception) { new EmbedGenerator(channel).text("You entered an invalid action").send(); return; }

		if (action.equalsIgnoreCase("list") || action.equalsIgnoreCase("show"))
		{
			EmbedGenerator embed = new EmbedGenerator(channel).title("Declines for " + guild.getName());
			StringBuilder description = new StringBuilder();

			for (String[] selectedAliases : Command_Utils.getDeclined(guild.getId()))
			{
				Role role;
				for (int i = 1; i < selectedAliases.length; i++)
				{
					role = Common.getRoleFromMention(guild, selectedAliases[0]);
					if (role != null)
					{
						description.append(role.getAsMention()).append(" ---> ").append(selectedAliases[i]).append("\n");
					}
				}
				description.append("\n");
			}
			if (description.length() == 0) description = new StringBuilder("No roles found.");
			embed.text(description.toString()).send();
			return;
		}

        Role role;
        try{ role = Common.getRoleFromMention(guild, args[1]); }
		catch(Exception exception) { role = null; }
		
		if(role == null)
		{
			new EmbedGenerator(channel).text("Mention a role to alias.").color(Color.RED).sendTemporary();
			return;
		}

        String alias;
        try { alias = args[2]; }
		catch(Exception exception) { new EmbedGenerator(channel).text("You entered an invalid alias").send(); return; }
		
		switch(action.toLowerCase())
		{
			case "add":
			case "accept":
			case "yes":
				Command_Utils.insertDecline(guild.getId(), role.getId(), alias);
				new EmbedGenerator(channel).text("Added Decline: " + alias + " for role: " + role.getAsMention()).sendTemporary();
				break;
				
			case "remove":
			case "delete":
				if(Command_Utils.removeDecline(guild.getId(), role.getId(), alias))
				{
					new EmbedGenerator(channel).text("Removed Decline: " + alias + " for role: " + role.getAsMention()).sendTemporary();
				}
				else
				{
					new EmbedGenerator(channel).text("Decline: " + alias + " not found for role: " + role.getAsMention()).sendTemporary();
				}
				break;
			default:
				break;
		}
	}
}
