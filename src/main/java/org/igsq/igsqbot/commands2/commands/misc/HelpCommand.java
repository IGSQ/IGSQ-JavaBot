package org.igsq.igsqbot.commands2.commands.misc;

import java.util.List;
import java.util.OptionalInt;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

public class HelpCommand extends NewCommand
{
    public HelpCommand()
    {
        super("Help", "Shows the help menu for this bot", "[page]");
        addAliases("help", "?", "howto", "commands");
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
            OptionalInt page = new Parser(args.get(0), ctx).parseAsUnsignedInt();
            if(page.isPresent())
            {
                if(page.getAsInt() + 1 > ctx.getIGSQBot().getHelpPages().size() + 1)
                {
                    ctx.replyError("That page does not exist");
                }
                else
                {
                    ctx.getChannel().sendMessage(ctx.getIGSQBot().getHelpPages().get(page.getAsInt() - 1).build()).queue();
                }
            }
        }
    }
}
