package org.igsq.igsqbot.commands2.subcommands.link;

import java.util.List;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.minecraft.MinecraftChecks;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.EmbedUtils;

public class LinkRemoveCommand extends NewCommand
{
    public LinkRemoveCommand(NewCommand parent)
    {
        super(parent, "remove", "Removes Minecraft links", "[mcUsername]");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        if(args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(ctx);
            return;
        }
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
