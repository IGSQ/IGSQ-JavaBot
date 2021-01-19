package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;

import org.igsq.igsqbot.commands.subcommands.level.LevelAddCommand;
import org.igsq.igsqbot.commands.subcommands.level.LevelBotCommand;
import org.igsq.igsqbot.commands.subcommands.level.LevelRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.level.LevelShowCommand;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.SyntaxException;

public class LevelCommand extends Command
{
    public LevelCommand()
    {
        super("Level", "Controls automatic role granting.", "[add / show / remove / bot]");
        addAliases("level");
        addFlags(CommandFlag.GUILD_ONLY);
        addChildren(
            new LevelAddCommand(this),
            new LevelBotCommand(this),
            new LevelShowCommand(this),
            new LevelRemoveCommand(this)
		);
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        throw new SyntaxException(ctx);
    }
}
