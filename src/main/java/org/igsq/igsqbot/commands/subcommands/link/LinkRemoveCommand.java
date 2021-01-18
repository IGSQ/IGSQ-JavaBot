package org.igsq.igsqbot.commands.subcommands.link;

import java.util.List;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.minecraft.MinecraftChecks;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.CommandChecks;

public class LinkRemoveCommand extends Command
{
	public LinkRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes Minecraft links.", "[mcUsername]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsEmpty(ctx);

		String arg = args.get(0);
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
}
