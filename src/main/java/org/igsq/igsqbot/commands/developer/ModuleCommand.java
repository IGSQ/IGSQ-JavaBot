package org.igsq.igsqbot.commands.developer;

import java.util.Arrays;
import java.util.List;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;

public class ModuleCommand extends Command
{
    @Override
    public void execute(List<String> args, CommandContext ctx)
    {
        if (args.size() != 2)
        {
            EmbedUtils.sendSyntaxError(ctx);
        }
        else if (ArrayUtils.isValueInArray(this.getAliases().toArray(), args.get(1).toLowerCase()))
        {
            ctx.replyError("You cannot disable that command.");
        }
        else if (args.get(0).equalsIgnoreCase("enable"))
        {
            enableModule(args.get(1), ctx);
        }
        else if (args.get(0).equalsIgnoreCase("disable"))
        {
            disableModule(args.get(1), ctx);
        }
        else
        {
            EmbedUtils.sendSyntaxError(ctx);
        }
    }

    @Override
    public String getName()
    {
        return "Module";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("module", "command");
    }

    @Override
    public String getDescription()
    {
        return "Disables / Enables the specified module.";
    }

    @Override
    public String getSyntax()
    {
        return "[enable|disable] [module]";
    }

    @Override
    public boolean canExecute(CommandContext ctx)
    {
        return ctx.isDeveloper();
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

    private void enableModule(String moduleName, CommandContext ctx)
    {
        NewCommand cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
        if (cmd == null)
        {
            ctx.replyError("The specified module: `" + moduleName + "` was not found");
        }
        else
        {
            cmd.setDisabled(false);
            ctx.replySuccess("Enabled module: `" + cmd.getName() + "`.");
            ctx.getIGSQBot().getLogger().warn("Module " + cmd.getName() + " was enabled.");
        }
    }

    private void disableModule(String moduleName, CommandContext ctx)
    {
        NewCommand cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
        if (cmd == null)
        {
            ctx.getIGSQBot().getLogger().warn("Module " + moduleName + " was enabled.");
        }
        else
        {
            cmd.setDisabled(true);
            ctx.replySuccess("Disabled module: `" + cmd.getName() + "`.");
            ctx.getIGSQBot().getLogger().warn("Module " + cmd.getName() + " was enabled.");
        }
    }
}
