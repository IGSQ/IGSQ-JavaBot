package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;
import org.igsq.igsqbot.commands.subcommands.info.BotInfoCommand;
import org.igsq.igsqbot.commands.subcommands.info.RoleInfoCommand;
import org.igsq.igsqbot.commands.subcommands.info.ServerInfoCommand;
import org.igsq.igsqbot.commands.subcommands.info.UserInfoCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;

@SuppressWarnings("unused")
public class InfoCommand extends Command
{
	public InfoCommand()
	{
		super("Info", "Provides information about things.", "[user/server/bot/role]");
		addAliases("info");
		addFlags(CommandFlag.GUILD_ONLY);
		addChildren(
				new UserInfoCommand(this),
				new BotInfoCommand(this),
				new RoleInfoCommand(this),
				new ServerInfoCommand(this));
	}

	@Override
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		this.getChildren().get(0).run(args, ctx, failure);
	}
}

