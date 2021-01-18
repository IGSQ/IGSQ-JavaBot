package org.igsq.igsqbot.commands.subcommands.blacklist;

import java.util.List;

import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandFlag;
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
    public void run(List<String> args, CommandContext ctx)
    {
        CommandChecks.argsEmpty(ctx);
        String phrase = ArrayUtils.arrayCompile(args, " ");
        BlacklistUtils.addPhrase(ctx.getGuild(), phrase, ctx.getIGSQBot());
        ctx.replySuccess("Added phrase ||" + phrase + "|| to the blacklist");
    }
}
