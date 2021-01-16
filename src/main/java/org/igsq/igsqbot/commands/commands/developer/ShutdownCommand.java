package org.igsq.igsqbot.commands.commands.developer;

import java.util.List;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

public class ShutdownCommand extends Command
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
