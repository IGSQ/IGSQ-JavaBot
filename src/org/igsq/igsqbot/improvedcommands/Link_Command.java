package org.igsq.igsqbot.improvedcommands;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.minecraft.Common_Minecraft;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.objects.ErrorHandler;

public class Link_Command extends Command
{

	private String[] args;
	private MessageChannel channel;
	private User author;

	public Link_Command()
	{
		super("link", new String[]{"mclink", "minecraft"}, "Controls Minecraft links.", new Permission[]{}, false,0);
	}

	private void showPending()
	{
		final ResultSet linked_accounts = Database.queryCommand("SELECT * FROM linked_accounts WHERE id = '" + author.getId() + "';");
		final StringBuilder embedDescription = new StringBuilder();
		String status;
		try 
		{
			while(linked_accounts.next())
			{
				ResultSet mc_accounts = Database.queryCommand("SELECT username FROM mc_accounts WHERE uuid = '" + linked_accounts.getString(2) + "';");
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
					embedDescription.append(mc_accounts.getString(1)).append(": ").append(status).append("\n");
				}
			}
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
		}
		
		if(embedDescription.length() == 0) embedDescription.append("No accounts pending");
		
		new EmbedGenerator(channel).title("All links for " + author.getAsTag()).text(embedDescription.toString()).send();
	}
	private void removeLink()
	{
		String mcAccount = "";
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
		
		
		try 
		{
			String uuid = Common_Minecraft.getUUIDFromName(mcAccount);
			String username = Common_Minecraft.getNameFromUUID(uuid);
			
			if(username == null)
			{
				new EmbedGenerator(channel).text("That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.").sendTemporary();
				return;
			}
			
			ResultSet linked_accounts = Database.queryCommand("SELECT id FROM linked_accounts WHERE uuid = '" + uuid + "';");
			if(linked_accounts.next())
			{
				id = linked_accounts.getString(1);
				if(id.equals(author.getId()))
				{
					Database.updateCommand("DELETE FROM linked_accounts WHERE uuid = '" + uuid + "';" );
					new EmbedGenerator(channel).text("Link removed for account: " + username).sendTemporary();
                }
				else
				{
					new EmbedGenerator(channel).text("That Minecraft account is not linked to your Discord.").sendTemporary();
                }
			}
			else
			{
				new EmbedGenerator(channel).text("That Minecraft account is not linked, please link before trying to delink.").sendTemporary();
            }
		} 
		catch (SQLException exception) 
		{
			new ErrorHandler(exception);
		}
	}

	private void addLink()
	{
		String mcAccount = "";
		try
		{
			mcAccount = args[1];
		}
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("You did not enter a Minecraft account").sendTemporary();
			return;
		}

		String uuid = Common_Minecraft.getUUIDFromName(mcAccount);
		String username = Common_Minecraft.getNameFromUUID(uuid);
		if(uuid != null)
		{
			boolean isWaiting = Database.scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + uuid +"' AND current_status = 'dwait';") > 0;
			boolean isAlreadyLinked = Database.scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE id = '" + author.getId() +"' AND current_status = 'linked';") > 0;
			boolean isUsersAccount = Database.scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + uuid +"' AND current_status = 'linked';") > 0;

			if(isUsersAccount)
			{
				new EmbedGenerator(channel).text("There is already an account linked to that Minecraft username.").sendTemporary();
				return;
			}
			if(isAlreadyLinked)
			{
				new EmbedGenerator(channel).text("There is already an account linked to your Discord, please delink first.").sendTemporary();
				return;
			}
			if(isWaiting)
			{
				Database.updateCommand("UPDATE linked_accounts SET current_status = 'linked' WHERE uuid = '" + uuid + "';");
				Database.updateCommand("DELETE FROM linked_accounts WHERE id = '" + author.getId() + "' AND current_status = 'dwait';");
				new EmbedGenerator(channel).text("Link confirmed for account: " + username).sendTemporary();
            }
			else
			{
				Database.updateCommand("INSERT INTO linked_accounts VALUES(null,'" + uuid + "','" + author.getId() + "','mwait');");
				new EmbedGenerator(channel).text("Link added for account: " + username).sendTemporary();
            }
		}
		else
		{
			new EmbedGenerator(channel).text("That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.").sendTemporary();
        }
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final String action;
		this.args = args;
		this.channel = ctx.getChannel();
		this.author = ctx.getAuthor();

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
		}
	}
}

	
