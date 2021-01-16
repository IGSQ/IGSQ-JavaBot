package org.igsq.igsqbot.commands2.commands.developer;

import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;

import java.util.List;

public class ShutdownCommand extends NewCommand
{
    public ShutdownCommand()
    {
        super("Shutdown", "Shuts the bot down gracefully", "[none]");
        addAliases("shutdown");
        developerOnly();
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        ctx.getIGSQBot().getDatabaseManager().close();
        ctx.getIGSQBot().getCommandHandler().close();
        ctx.getIGSQBot().getTaskHandler().close();
        ctx.getIGSQBot().getMinecraft().close();

        ctx.getJDA().shutdown();

        ctx.getIGSQBot().getLogger().warn("-- IGSQBot was shutdown using shutdown command.");
        ctx.getIGSQBot().getLogger().warn("-- Issued by: " + ctx.getAuthor().getAsTag());
        if(ctx.getGuild() != null)
        {
            ctx.getIGSQBot().getLogger().warn("-- In guild: " + ctx.getGuild().getName());
        }
        else
        {
            ctx.getIGSQBot().getLogger().warn("-- In guild: " + "Shutdown in DMs.");
        }
        System.exit(0);
    }
}
