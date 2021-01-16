package org.igsq.igsqbot.commands2.subcommands.info;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

public class RoleInfoCommand extends NewCommand
{
    public RoleInfoCommand(NewCommand parent)
    {
        super(parent, "role", "Shows information about a role", "[role]");
    }
    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        if(args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(ctx);
        }
        else
        {
            new Parser(ArrayUtils.arrayCompile(args.subList(1, args.size()), " "), ctx).parseAsRole(role ->
            {
                org.igsq.igsqbot.entities.info.RoleInfo roleInfo = new org.igsq.igsqbot.entities.info.RoleInfo(role);

                roleInfo.getMembers().onSuccess(members ->
                {
                    int size = members.size();
                    StringBuilder text = new StringBuilder();

                    if(size > 5)
                    {
                        members = members.subList(0, 5);
                    }

                    text.append(roleInfo.getAsMention())
                            .append(" | ")
                            .append(size)
                            .append(" members")
                            .append("\n")
                            .append("**Members:**")
                            .append("\n");

                    members.forEach(member -> text.append(member.getAsMention()).append(" "));

                    ctx.getChannel().sendMessage(new EmbedBuilder()
                            .setDescription(text.toString())
                            .setColor(Constants.IGSQ_PURPLE)
                            .build()).queue();
                });
            });
        }
    }
}
