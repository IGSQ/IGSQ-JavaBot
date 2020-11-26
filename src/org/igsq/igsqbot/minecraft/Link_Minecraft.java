package org.igsq.igsqbot.minecraft;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.EmbedGenerator;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Link_Minecraft 
{
	private User user;
	private String[] args;
	private Message message;
	private MessageChannel channel;

	public Link_Minecraft(MessageReceivedEvent event) 
	{
		this.user = event.getAuthor();
		this.message = event.getMessage();
		this.channel = event.getChannel();
		
		this.args = event.getMessage().getContentRaw().toLowerCase().split(" ", 3);
		this.args = Common.depend(args, 0);
		linkQuery();
	}
	
	private void linkQuery()
	{
		if(!user.isBot() && message.isFromType(ChannelType.TEXT)) link();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	private void link()
	{
		String action;
		try
		{
			action = args[0];
		}
		catch(Exception exception) 
		{
			new EmbedGenerator(channel).text("You entered an invalid action").sendTemporary(); 
			return;
		}
		
		switch(action.toLowerCase())
		{
			case "add":
			case "new":
				addLink();
				break;
			case "remove":
			case "delete":
				removeLink();
				break;
			case "show":
			case "list":
			case "pending":
				showPending();
				break;
			default:
				new EmbedGenerator(channel).text("You entered an invalid action").sendTemporary(); 
				return;
		}
	}
	private void showPending()
	{
		ResultSet linked_accounts = Database.QueryCommand("SELECT * FROM linked_accounts WHERE id = '" + user.getId() + "';");
		String embedDescription = "";
		String status;
		try 
		{
			while(linked_accounts.next())
			{
				ResultSet mc_accounts = Database.QueryCommand("SELECT username FROM mc_accounts WHERE uuid = '" + linked_accounts.getString(2) + "';");
				if(mc_accounts.next())
				{
					switch(linked_accounts.getString(4).toLowerCase())
					{
					case "mwait":
						status = "Pending Minecraft confirmation.";
						break;
					case "dwait":
						status = "Pending Discord confirmation.";
						break;
					case "linked":
						status = "Linked!";
						break;
					default:
						status = "Status not found / invalid";
						break;
					}
					embedDescription += mc_accounts.getString(1) + ": " + status + "\n";
				}
			}
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
		}
		
		if(embedDescription.isEmpty()) embedDescription = "No accounts pending";
		
		new EmbedGenerator(channel).title("All links for " + user.getAsTag()).text(embedDescription).send();
	}
	private void removeLink()
	{
		String mcAccount = "";
		String uuid = "";
		String id = "";
		try
		{
			mcAccount = args[1];
		}
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("You did not enter a Minecraft account").sendTemporary(); 
			return;
		}
		
		ResultSet mc_accounts = Database.QueryCommand("SELECT uuid FROM mc_accounts WHERE username = '" + mcAccount + "';");
		
		try 
		{
			if(mc_accounts.next())
			{
				uuid = mc_accounts.getString(1);
			}
			else
			{
				new EmbedGenerator(channel).text("That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.").sendTemporary();
				return;
			}
			
			ResultSet linked_accounts = Database.QueryCommand("SELECT id FROM linked_accounts WHERE uuid = '" + uuid + "';");
			

			if(linked_accounts.next())
			{
				id = linked_accounts.getString(1);
				if(id.equals(user.getId()))
				{
					Database.UpdateCommand("DELETE FROM linked_accounts WHERE uuid = '" + uuid + "';" );
					new EmbedGenerator(channel).text("Link removed for account: " + mcAccount).sendTemporary(); 
					return;
				}
				else
				{
					new EmbedGenerator(channel).text("That Minecraft account is not linked to your Discord.").sendTemporary(); 
					return;
				}
			}
			else
			{
				new EmbedGenerator(channel).text("That Minecraft account is not linked, please link before trying to delink.").sendTemporary(); 
				return;
			}
		} 
		catch (SQLException exception) 
		{
			
		}
		
		Database.UpdateCommand("INSERT INTO linked_accounts VALUES(null,'" + uuid + "','" + user.getId() + "','mwait');");
		new EmbedGenerator(channel).text("Link added for account: " + mcAccount).sendTemporary(); 
	}
	
	private void addLink()
	{
		String mcAccount = "";
		String uuid = "";
		try
		{
			mcAccount = args[1];
		}
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("You did not enter a Minecraft account").sendTemporary(); 
			return;
		}
		
		ResultSet mc_accounts = Database.QueryCommand("SELECT uuid FROM mc_accounts WHERE username = '" + mcAccount + "';");
		try 
		{
			if(mc_accounts.next())
			{
				uuid = mc_accounts.getString(1);
				boolean isWaiting = Database.ScalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + uuid +"' AND current_status = 'dwait';") > 0;
				boolean isAlreadyLinked = Database.ScalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE id = '" + user.getId() +"' AND current_status = 'linked';") > 0;
				if(isAlreadyLinked)
				{
					new EmbedGenerator(channel).text("There is already an account linked to your Discord, please delink first.").sendTemporary(); 
					return;
				}
				if(isWaiting)
				{
					Database.UpdateCommand("UPDATE linked_accounts SET current_status = 'linked' WHERE uuid = '" + uuid + "';");
					Database.UpdateCommand("DELETE FROM linked_accounts WHERE id = '" + user.getId() + "' AND current_status = 'dwait';");
					new EmbedGenerator(channel).text("Link confirmed for account: " + mcAccount).sendTemporary(); 
				}
				else
				{
					Database.UpdateCommand("INSERT INTO linked_accounts VALUES(null,'" + uuid + "','" + user.getId() + "','mwait');");
					new EmbedGenerator(channel).text("Link added for account: " + mcAccount).sendTemporary(); 
				}
			}
			else
			{
				new EmbedGenerator(channel).text("That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.").sendTemporary(); 
				return;
			}
		} 
		catch (SQLException exception) 
		{
			
		}
	}
}
	
