package org.igsq.igsqbot.commands.subcommands.link;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
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
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(ctx, failure)) return;

		User author = ctx.getAuthor();
		Minecraft minecraft = ctx.getIGSQBot().getMinecraft();
		String arg = args.get(0);

		if(!MinecraftChecks.isAccountExist(arg, minecraft))
		{
			failure.accept(new CommandResultException("Account **" + arg + "** does not exist. Please ensure you have played on our server."));
			return;
		}
		String uuid = MinecraftUtils.getUUIDByName(arg, minecraft);
		String account = MinecraftUtils.getName(uuid, minecraft);

		if(MinecraftChecks.isAccountLinked(uuid, minecraft))
		{
			failure.accept(new CommandResultException("Account **" + account + "** is already linked."));
			return;
		}


		if(MinecraftChecks.isDuplicate(uuid, author.getId(), minecraft))
		{
			failure.accept(new CommandResultException("You cannot make duplicate link requests."));
			return;
		}
		if(MinecraftChecks.isPendingDiscord(uuid, minecraft))
		{
			MinecraftUtils.updateLink(uuid, author.getId(), minecraft);
			ctx.replySuccess("Confirmed link for account **" + account + "**");
		}
		if(MinecraftChecks.isUserLinked(author.getId(), minecraft))
		{
			failure.accept(new CommandResultException("You are already linked to an account."));
			return;
		}

		MinecraftUtils.insertLink(uuid, author.getId(), minecraft);
		ctx.replySuccess("Added link for account **" + account + "** confirm it in Minecraft now.");


	}
}