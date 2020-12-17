package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.minecraft.CommonMinecraft;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.EmbedUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LinkCommand extends Command
{

	private List<String> args;
	private MessageChannel channel;
	private User author;

	public LinkCommand()
	{
		super("Link", new String[]{"link", "mclink", "minecraft"}, "Controls Minecraft links.","[add|remove][mcName] | [list]", new Permission[]{}, false,0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final String action;
		this.args = args;
		this.channel = ctx.getChannel();
		this.author = ctx.getAuthor();

		try
		{
			action = args.get(0);
		}
		catch(Exception exception)
		{
			EmbedUtils.sendError(channel, "You entered an invalid action");
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
				EmbedUtils.sendSyntaxError(channel,this);
		}
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
						status = "pending Minecraft confirmation.";
						break;
					case "dwait":
						status = "pending Discord confirmation.";
						break;
					case "linked":
						status = "linked!";
						break;
					default:
						status = "status not found / invalid";
						break;
					}
					embedDescription.append("**").append(mc_accounts.getString(1)).append("**: ").append(status).append("\n");
				}
			}
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
		}
		
		if(embedDescription.length() == 0) embedDescription.append("No accounts pending");
		
		new EmbedGenerator(channel)
				.title("All links for " + author.getAsTag())
				.text(embedDescription.toString())
				.color(EmbedUtils.IGSQ_PURPLE)
				.send();
	}
	private void removeLink()
	{
		String mcAccount = "";
		String id = "";
		try
		{
			mcAccount = args.get(1);
		}
		catch(Exception exception)
		{
			EmbedUtils.sendSyntaxError(channel,this);
			return;
		}

		try 
		{
			String uuid = CommonMinecraft.getUUIDFromName(mcAccount);
			String username = CommonMinecraft.getNameFromUUID(uuid);
			
			if(username == null)
			{
				EmbedUtils.sendError(channel, "That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.");
				return;
			}
			
			ResultSet linked_accounts = Database.queryCommand("SELECT id FROM linked_accounts WHERE uuid = '" + uuid + "';");
			if(linked_accounts.next())
			{
				id = linked_accounts.getString(1);
				if(id.equals(author.getId()))
				{
					Database.updateCommand("DELETE FROM linked_accounts WHERE uuid = '" + uuid + "';" );
					EmbedUtils.sendSuccess(channel, "Link removed for account: " + username);
                }
				else
				{
					EmbedUtils.sendError(channel, "That Minecraft account is not linked to your Discord.");
                }
			}
			else
			{
				EmbedUtils.sendError(channel, "That Minecraft account is not linked, please link before trying to delink.");
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
			mcAccount = args.get(1);
		}
		catch(Exception exception)
		{
			EmbedUtils.sendSyntaxError(channel,this);
			return;
		}

		String uuid = CommonMinecraft.getUUIDFromName(mcAccount);
		String username = CommonMinecraft.getNameFromUUID(uuid);
		if(uuid != null)
		{
			boolean isWaiting = Database.scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + uuid +"' AND current_status = 'dwait';") > 0;
			boolean isAlreadyLinked = Database.scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE id = '" + author.getId() +"' AND current_status = 'linked';") > 0;
			boolean isUsersAccount = Database.scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + uuid +"' AND current_status = 'linked';") > 0;

			if(isUsersAccount)
			{
				EmbedUtils.sendError(channel, "There is already an account linked to that Minecraft username.");
			}
			else if(isAlreadyLinked)
			{
				EmbedUtils.sendError(channel, "There is already an account linked to your Discord, please delink first.");
			}
			else if(isWaiting)
			{
				Database.updateCommand("UPDATE linked_accounts SET current_status = 'linked' WHERE uuid = '" + uuid + "';");
				Database.updateCommand("DELETE FROM linked_accounts WHERE id = '" + author.getId() + "' AND current_status = 'dwait';");
				EmbedUtils.sendSuccess(channel, "Link confirmed for account: " + username);
            }
			else
			{
				Database.updateCommand("INSERT INTO linked_accounts VALUES(null,'" + uuid + "','" + author.getId() + "','mwait');");
				EmbedUtils.sendSuccess(channel, "Link added for account: " + username);
            }
		}
		else
		{
			EmbedUtils.sendError(channel, "That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.");
        }
	}
}

	
