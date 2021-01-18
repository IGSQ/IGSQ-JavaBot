package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.commands.subcommands.vote.VoteCloseCommand;
import org.igsq.igsqbot.commands.subcommands.vote.VoteCreateCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.exception.SyntaxException;

@SuppressWarnings("unused")
public class VoteCommand extends Command
{
    public VoteCommand()
    {
        super("Vote", "Controls voting", "[create | close]");
        addAliases("vote");
        addMemberPermissions(Permission.MANAGE_SERVER);
        addChildren(
                new VoteCreateCommand(this),
                new VoteCloseCommand(this)
        );
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
		throw new SyntaxException(ctx);
    }
}
