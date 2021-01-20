package org.igsq.igsqbot.commands.commands.developer;

import java.util.List;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;

@SuppressWarnings("unused")
public class ShutdownCommand extends Command
{
	public ShutdownCommand()
	{
		super("Shutdown", "Shuts the bot down gracefully.", "[none]");
		addFlags(CommandFlag.DEVELOPER_ONLY);
		addAliases("shutdown", "die");
	}

	@Override
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		ctx.getIGSQBot().getDatabaseHandler().close();
		ctx.getIGSQBot().getMinecraft().close();
		ctx.getIGSQBot().getTaskHandler().close();

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
