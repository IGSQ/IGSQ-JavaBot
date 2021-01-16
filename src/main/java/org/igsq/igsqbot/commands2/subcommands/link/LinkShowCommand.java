package org.igsq.igsqbot.commands2.subcommands.link;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.UserUtils;

public class LinkShowCommand extends NewCommand
{
    public LinkShowCommand(NewCommand parent)
    {
        super(parent, "show", "Shows Minecraft links", "[none]");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        Minecraft minecraft = ctx.getIGSQBot().getMinecraft();
        if(ctx.hasPermission(Permission.KICK_MEMBERS) && !args.isEmpty())
        {
            String arg = args.get(0);
            new Parser(arg, ctx).parseAsUser(user ->
            {
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
        else
        {
            List<MinecraftUtils.Link> links = MinecraftUtils.getLinks(ctx.getAuthor().getId(), minecraft);
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
                    .setTitle("Links for user: " + ctx.getAuthor().getAsTag())
                    .setDescription(text.length() == 0 ? "No links found" : text.toString())
                    .setColor(Constants.IGSQ_PURPLE)
                    .build()).queue();
        }
    }
}
