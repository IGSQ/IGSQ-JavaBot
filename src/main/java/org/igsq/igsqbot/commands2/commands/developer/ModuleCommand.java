package org.igsq.igsqbot.commands2.commands.developer;

import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;

public class ModuleCommand extends NewCommand
{
    public ModuleCommand()
    {
        super("Module", "Disables / Enables the specified module.", "[enable|disable] [module]");
        addAliases("module", "command");
        addChildren(
                new ModuleEnable(this),
                new ModuleDisable(this)
                );
        developerOnly();
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        EmbedUtils.sendSyntaxError(ctx);
    }

    public static class ModuleEnable extends NewCommand
    {
        public ModuleEnable(NewCommand parent)
        {
            super(parent, "enable", "Enables a module", "[module-name]");
        }

        @Override
        public void run(List<String> args, CommandContext ctx)
        {
            if(args.isEmpty())
            {
                EmbedUtils.sendSyntaxError(ctx);
                return;
            }
            String moduleName = args.get(0);
            Command cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
            if(cmd == null)
            {
                ctx.getIGSQBot().getLogger().warn("Module " + moduleName + " was enabled.");
            }
            else
            {
                cmd.setDisabled(false);
                ctx.replySuccess("Disabled module: `" + cmd.getName() + "`.");
                ctx.getIGSQBot().getLogger().warn("Module " + cmd.getName() + " was enabled.");
            }
        }
    }

    public static class ModuleDisable extends NewCommand
    {
        public ModuleDisable(NewCommand parent)
        {
            super(parent, "disable", "Disables a module", "[module-name]");
        }

        @Override
        public void run(List<String> args, CommandContext ctx)
        {
            if(args.isEmpty())
            {
                EmbedUtils.sendSyntaxError(ctx);
                return;
            }
            String moduleName = args.get(0);
            Command cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
            if(cmd == null)
            {
                ctx.getIGSQBot().getLogger().warn("Module " + moduleName + " was enabled.");
            }
            else
            {
                cmd.setDisabled(true);
                ctx.replySuccess("Disabled module: `" + cmd.getName() + "`.");
                ctx.getIGSQBot().getLogger().warn("Module " + cmd.getName() + " was disabled.");
            }
        }
    }
}
