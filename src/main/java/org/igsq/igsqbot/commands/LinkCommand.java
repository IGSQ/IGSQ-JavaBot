package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Arrays;
import java.util.List;

public class LinkCommand extends Command
{
	private List<String> args;
	private MessageChannel channel;
	private User author;

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		this.args = args;
		this.channel = ctx.getChannel();
		this.author = ctx.getAuthor();

		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			switch(args.get(0).toLowerCase())
			{
				case "add", "new" -> addLink(ctx.getIGSQBot());
				case "remove", "delete" -> removeLink(ctx.getIGSQBot());
				case "show", "list", "pending" -> showPending(ctx.getIGSQBot());
				default -> EmbedUtils.sendSyntaxError(channel, this);
			}
		}
	}

	@Override
	public String getName()
	{
		return "Link";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("link", "mclink", "minecraft");
	}

	@Override
	public String getDescription()
	{
		return "Controls Minecraft links.";
	}

	@Override
	public String getSyntax()
	{
		return "[add|remove][mcName] | [list]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.getIGSQBot().getDatabase().isOnline();
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}

	private void showPending(IGSQBot igsqBot)
	{
		StringBuilder embedDescription = new StringBuilder();
		MinecraftUtils.fetchLinks(author.getId(), igsqBot).forEach((name, state) ->
		{
			String status = switch(state.toLowerCase())
					{
						case "mwait" -> "pending Minecraft confirmation.";
						case "dwait" -> "pending Discord confirmation.";
						case "linked" -> "linked!";
						default -> "status not found / invalid";
					};
			embedDescription.append("**")
					.append(name)
					.append("**: ")
					.append(status)
					.append("\n");
		});

		if(embedDescription.length() == 0) embedDescription.append("No links found.");
		channel.sendMessage(new EmbedBuilder()
				.setTitle("All links for " + author.getAsTag())
				.setDescription(embedDescription.toString())
				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue();
	}

	private void removeLink(IGSQBot igsqBot)
	{
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			String mcAccount = args.get(1);
			String uuid = MinecraftUtils.getUUIDFromName(mcAccount, igsqBot);
			String username = MinecraftUtils.getNameFromUUID(uuid, igsqBot);
			String id = MinecraftUtils.getIDFromUUID(uuid, igsqBot);

			if(username == null)
			{
				EmbedUtils.sendError(channel, "That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.");
				return;
			}
			if(id == null)
			{
				EmbedUtils.sendError(channel, "That Minecraft account is not linked, please link before trying to delink.");
				return;
			}

			if(id.equals(author.getId()))
			{
				MinecraftUtils.removeLinkedAccount(uuid, igsqBot);
				EmbedUtils.sendSuccess(channel, "Link removed for account: " + username);
			}
			else
			{
				EmbedUtils.sendError(channel, "That Minecraft account is not linked to your Discord.");
			}
		}
	}

	private void addLink(IGSQBot igsqBot)
	{
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			String mcAccount = args.get(1);
			String uuid = MinecraftUtils.getUUIDFromName(mcAccount, igsqBot);
			final String username = MinecraftUtils.getNameFromUUID(uuid, igsqBot);
			if(uuid != null)
			{
				boolean isWaiting = igsqBot.getDatabase().scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + uuid + "' AND current_status = 'dwait';") > 0;
				boolean isAlreadyLinked = igsqBot.getDatabase().scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE id = '" + author.getId() + "' AND current_status = 'linked';") > 0;
				boolean isUsersAccount = igsqBot.getDatabase().scalarCommand("SELECT COUNT(*) FROM linked_accounts WHERE uuid = '" + uuid + "' AND current_status = 'linked';") > 0;

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
					igsqBot.getDatabase().updateCommand("UPDATE linked_accounts SET current_status = 'linked' WHERE uuid = '" + uuid + "';");
					igsqBot.getDatabase().updateCommand("DELETE FROM linked_accounts WHERE id = '" + author.getId() + "' AND current_status = 'dwait';");
					EmbedUtils.sendSuccess(channel, "Link confirmed for account: " + username);
				}
				else
				{
					igsqBot.getDatabase().updateCommand("INSERT INTO linked_accounts VALUES(null,'" + uuid + "','" + author.getId() + "','mwait');");
					EmbedUtils.sendSuccess(channel, "Link added for account: " + username);
				}
			}
			else
			{
				EmbedUtils.sendError(channel, "That Minecraft account does not exists on our system, please ensure you have played on the server before attempting a link.");
			}
		}
	}
}

	
