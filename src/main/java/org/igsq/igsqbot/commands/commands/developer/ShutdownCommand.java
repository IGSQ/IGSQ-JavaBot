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
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		cmd.getIGSQBot().getDatabaseHandler().close();
		cmd.getIGSQBot().getMinecraft().close();
		cmd.getIGSQBot().getTaskHandler().close();

		cmd.getJDA().shutdown();

		cmd.getIGSQBot().getLogger().warn("-- IGSQBot was shutdown using shutdown command.");
		cmd.getIGSQBot().getLogger().warn("-- Issued by: " + cmd.getAuthor().getAsTag());
		if(cmd.getGuild() != null)
		{
			cmd.getIGSQBot().getLogger().warn("-- In guild: " + cmd.getGuild().getName());
		}
		else
		{
			cmd.getIGSQBot().getLogger().warn("-- In guild: " + "Shutdown in DMs.");
		}
		System.exit(0);
	}
}
