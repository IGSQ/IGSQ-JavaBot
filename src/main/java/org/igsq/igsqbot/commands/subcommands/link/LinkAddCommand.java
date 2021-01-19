package org.igsq.igsqbot.commands.subcommands.link;

import java.util.List;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.minecraft.MinecraftChecks;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.CommandChecks;

public class LinkAddCommand extends Command
{
	public LinkAddCommand(Command parent)
	{
		super(parent, "add", "Adds or confirms Minecraft links.", "[mcUsername]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsEmpty(ctx);

		User author = ctx.getAuthor();
		Minecraft minecraft = ctx.getIGSQBot().getMinecraft();
		String arg = args.get(0);

		if(!MinecraftChecks.isAccountExist(arg, minecraft))
		{
			throw new CommandResultException("Account **" + arg + "** does not exist. Please ensure you have played on our server.");
		}
		String uuid = MinecraftUtils.getUUIDByName(arg, minecraft);
		String account = MinecraftUtils.getName(uuid, minecraft);

		if(MinecraftChecks.isAccountLinked(uuid, minecraft))
		{
			throw new CommandResultException("Account **" + account + "** is already linked.");
		}
		else
		{
			if(MinecraftChecks.isDuplicate(uuid, author.getId(), minecraft))
			{
				throw new CommandResultException("You cannot make duplicate link requests.");
			}
			else if(MinecraftChecks.isPendingDiscord(uuid, minecraft))
			{
				MinecraftUtils.updateLink(uuid, author.getId(), minecraft);
				ctx.replySuccess("Confirmed link for Account **" + account + "**");
			}
			else if(MinecraftChecks.isUserLinked(author.getId(), minecraft))
			{
				throw new CommandResultException("You are already linked to an account.");
			}
			else
			{
				MinecraftUtils.insertLink(uuid, author.getId(), minecraft);
				ctx.replySuccess("Added link for Account **" + account + "** confirm it in Minecraft now.");
			}
		}
	}
}