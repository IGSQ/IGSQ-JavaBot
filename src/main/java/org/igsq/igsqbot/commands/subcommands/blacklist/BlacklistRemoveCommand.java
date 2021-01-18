package org.igsq.igsqbot.commands.subcommands.blacklist;

import java.util.List;

import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.CommandChecks;

public class BlacklistRemoveCommand extends Command
{
    public BlacklistRemoveCommand(Command parent)
    {
        super(parent, "remove", "Removes a phrase from the blacklist.", "[phrase]");
        addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE, CommandFlag.BLACKLIST_BYPASS);
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        CommandChecks.argsEmpty(ctx);
        String phrase = ArrayUtils.arrayCompile(args, " ");
        if(BlacklistUtils.removePhrase(ctx.getGuild(), phrase, ctx.getIGSQBot()))
        {
            ctx.replySuccess("Removed phrase ||" + phrase + "|| from the blacklist");
        }
        else
        {
            ctx.replyError("Phrase ||" + phrase + "|| not found in the blacklist.");
        }
    }
}
