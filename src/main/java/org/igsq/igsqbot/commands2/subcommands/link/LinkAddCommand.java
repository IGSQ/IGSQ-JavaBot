package org.igsq.igsqbot.commands2.subcommands.link;

import java.util.List;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.minecraft.MinecraftChecks;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.EmbedUtils;

public class LinkAddCommand extends NewCommand
{
    public LinkAddCommand(NewCommand parent)
    {
        super(parent, "add", "Adds or confirms Minecraft links", "[mcUsername]");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        if(args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(ctx);
            return;
        }

        User author = ctx.getAuthor();
        Minecraft minecraft = ctx.getIGSQBot().getMinecraft();
        String arg = args.get(0);

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
}