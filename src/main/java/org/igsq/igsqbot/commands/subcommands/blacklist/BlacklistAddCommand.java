package org.igsq.igsqbot.commands.subcommands.blacklist;

import java.util.List;

import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.CommandChecks;

public class BlacklistAddCommand extends Command
{
    public BlacklistAddCommand(Command parent)
    {
        super(parent, "add", "Adds a phrase to the blacklist.", "[phrase]");
        addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
    }

    @Override
    public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
    {
        if(CommandChecks.argsEmpty(ctx, failure)) return;
        String phrase = ArrayUtils.arrayCompile(args, " ");
        BlacklistUtils.addPhrase(ctx.getGuild(), phrase, ctx.getIGSQBot());
        ctx.replySuccess("Added phrase ||" + phrase + "|| to the blacklist");
    }
}
