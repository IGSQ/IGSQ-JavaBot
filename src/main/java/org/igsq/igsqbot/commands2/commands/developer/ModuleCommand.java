package org.igsq.igsqbot.commands2.commands.developer;

import java.util.List;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.util.EmbedUtils;

public class ModuleCommand extends NewCommand
{
    public ModuleCommand()
    {
        super("Module", "Disables / Enables the specified module.", "[enable|disable] [module]");
        addAliases("module", "command");
        addChildren(
                new ModuleEnableCommand(this),
                new ModuleDisableCommand(this)
                );
        developerOnly();
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        EmbedUtils.sendSyntaxError(ctx);
    }

    public static class ModuleEnableCommand extends NewCommand
    {
        public ModuleEnableCommand(NewCommand parent)
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
            NewCommand cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
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

    public static class ModuleDisableCommand extends NewCommand
    {
        public ModuleDisableCommand(NewCommand parent)
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
            NewCommand cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
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
