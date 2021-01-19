package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.commands.subcommands.blacklist.BlacklistAddCommand;
import org.igsq.igsqbot.commands.subcommands.blacklist.BlacklistRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.blacklist.BlacklistShowCommand;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.exception.SyntaxException;

@SuppressWarnings("unused")
public class BlacklistCommand extends Command
{
    public BlacklistCommand()
    {
        super("Blacklist", "Controls the blacklists.", "[add / remove / show]");
        addAliases("blacklist");
        addMemberPermissions(Permission.MANAGE_SERVER);
        addChildren(
        		new BlacklistAddCommand(this),
				new BlacklistRemoveCommand(this),
				new BlacklistShowCommand(this)
		);
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
		throw new SyntaxException(ctx);
    }
}