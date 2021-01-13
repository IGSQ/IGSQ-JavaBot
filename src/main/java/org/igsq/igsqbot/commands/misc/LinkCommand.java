package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.minecraft.MinecraftChecks;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Arrays;
import java.util.List;

public class LinkCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			String action = args.get(0);

			if(action.equalsIgnoreCase("add"))
			{
				addLink(args.get(1), ctx);
			}
			else if(action.equalsIgnoreCase("remove"))
			{
				removeLink(args.get(1), ctx);
			}
			else if(action.equalsIgnoreCase("show"))
			{
				showLink(args.get(1), ctx);
			}
			else
			{
				EmbedUtils.sendSyntaxError(ctx);
			}
		}
	}

	private void addLink(String arg, CommandContext ctx)
	{
		User author = ctx.getAuthor();
		Minecraft minecraft = ctx.getIGSQBot().getMinecraft();

		if(!MinecraftChecks.isAccountExist(arg, minecraft))
		{
			ctx.replyError("Account **" + arg + "** does not exist. Please ensure you have played on our server.");
			return;
		}
		String uuid = MinecraftUtils.getUUIDByName(arg, minecraft);
		String account = MinecraftUtils.getName(uuid, minecraft);

		if(MinecraftChecks.isAccountLinked(uuid, minecraft))
		{
			ctx.replyError("Account **" + account + "** is already linked.");
		}
		else
		{
			if(MinecraftChecks.isDuplicate(uuid, author.getId(), minecraft))
			{
				ctx.replyError("You cannot make duplicate link requests.");
			}
			else if(MinecraftChecks.isPendingDiscord(uuid, minecraft))
			{
				MinecraftUtils.updateLink(uuid, author.getId(), minecraft);
				ctx.replySuccess("Confirmed link for Account **" + account + "**");
			}
			else if(MinecraftChecks.isUserLinked(author.getId(), minecraft))
			{
				ctx.replyError("You are already linked to an account.");
			}
			else
			{
				MinecraftUtils.insertLink(uuid, author.getId(), minecraft);
				ctx.replySuccess("Added link for Account **" + account + "** confirm it in Minecraft now.");
			}
		}
	}

	private void removeLink(String arg, CommandContext ctx)
	{
		User author = ctx.getAuthor();
		Minecraft minecraft = ctx.getIGSQBot().getMinecraft();

		if(!MinecraftChecks.isAccountExist(arg, minecraft))
		{
			ctx.replyError("Account **" + arg + "** does not exist. Please ensure you have played on our server.");
			return;
		}
		String uuid = MinecraftUtils.getUUIDByName(arg, minecraft);
		String account = MinecraftUtils.getName(uuid, minecraft);

		if(!MinecraftChecks.isAccountLinked(uuid, minecraft))
		{
			ctx.replyError("Account **" + account + "** is not linked.");
		}
		else if(!MinecraftChecks.isOwnerOfAccount(uuid, author.getId(), minecraft))
		{
			ctx.replyError("Account **" + account + "** does not belong to you.");
		}
		else
		{
			MinecraftUtils.removeLink(uuid, author.getId(), minecraft);
			ctx.replySuccess("Removed link **" + account + "**");
		}
	}

	private void showLink(String arg, CommandContext ctx)
	{
		new Parser(arg, ctx).parseAsUser(user ->
		{
			Minecraft minecraft = ctx.getIGSQBot().getMinecraft();
			List<MinecraftUtils.Link> links = MinecraftUtils.getLinks(user.getId(), minecraft);
			StringBuilder text = new StringBuilder();

			for(MinecraftUtils.Link link : links)
			{
				text
						.append(UserUtils.getAsMention(link.getId()))
						.append(" -- ")
						.append(MinecraftUtils.getName(link.getUuid(), minecraft))
						.append(" -- ")
						.append(MinecraftUtils.prettyPrintLinkState(link.getLinkState()))
						.append("\n");
			}

			ctx.getChannel().sendMessage(new EmbedBuilder()
					.setTitle("Links for user: " + user.getAsTag())
					.setDescription(text.length() == 0 ? "No links found" : text.toString())
					.setColor(Constants.IGSQ_PURPLE)
					.build()).queue();
		});
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
		return "[add|remove][mcName] | [show][user]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.getIGSQBot().getMinecraft().getDatabaseHandler().isOnline();
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public long getCooldown()
	{
		return 0;
	}
}

	
