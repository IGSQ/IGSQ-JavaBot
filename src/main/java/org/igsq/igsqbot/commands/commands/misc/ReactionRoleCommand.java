package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;

import org.igsq.igsqbot.commands.subcommands.reactionrole.ReactionRoleAddCommand;
import org.igsq.igsqbot.commands.subcommands.reactionrole.ReactionRoleRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.reactionrole.ReactionRoleShowCommand;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandFlag;
import org.igsq.igsqbot.util.EmbedUtils;

@SuppressWarnings("unused")
public class ReactionRoleCommand extends Command
{
    public ReactionRoleCommand()
    {
        super("Reaction Role", "Controls reaction roles", "[add | remove | show]");
        addAliases("rr", "reactionrole");
        addFlags(CommandFlag.GUILD_ONLY);
        addChildren(
                new ReactionRoleAddCommand(this),
                new ReactionRoleRemoveCommand(this),
                new ReactionRoleShowCommand(this)
        );
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
		EmbedUtils.sendSyntaxError(ctx);
    }
}