package org.igsq.igsqbot.commands.commands.developer;

import java.util.List;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.SyntaxException;
import org.igsq.igsqbot.util.CommandChecks;

@SuppressWarnings("unused")
public class ModuleCommand extends Command
{
	public ModuleCommand()
	{
		super("Module", "Disables / Enables the specified module.", "[enable|disable] [module]");
		addFlags(CommandFlag.DEVELOPER_ONLY);
		addAliases("module", "command");
		addChildren(
				new ModuleEnableCommand(this),
				new ModuleDisableCommand(this)
		);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		throw new SyntaxException(ctx);
	}

	public static class ModuleEnableCommand extends Command
	{
		public ModuleEnableCommand(Command parent)
		{
			super(parent, "enable", "Enables a module", "[module-name]");
		}

		@Override
		public void run(List<String> args, CommandContext ctx)
		{
			CommandChecks.argsEmpty(ctx);
			String moduleName = args.get(0);
			Command cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
			if(cmd == null)
			{
				ctx.replyError("Module " + moduleName + " was not found");
			}
			else
			{
				cmd.setDisabled(false);
				ctx.replySuccess("Disabled module: `" + cmd.getName() + "`.");
				ctx.getIGSQBot().getLogger().warn("Module " + cmd.getName() + " was enabled.");
			}
		}
	}

	public static class ModuleDisableCommand extends Command
	{
		public ModuleDisableCommand(Command parent)
		{
			super(parent, "disable", "Disables a module", "[module-name]");
		}

		@Override
		public void run(List<String> args, CommandContext ctx)
		{
			CommandChecks.argsEmpty(ctx);
			String moduleName = args.get(0);
			Command cmd = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(moduleName);
			if(cmd == null)
			{
				ctx.replyError("Module " + moduleName + " was not found");
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
