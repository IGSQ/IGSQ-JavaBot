package org.igsq.igsqbot.commands.misc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

public class ReactionRoleCommand extends Command
{
    @Override
    public void execute(List<String> args, CommandContext ctx)
    {
        //TO BE IMPLEMENTED
    }

    @Override
    public String getName()
    {
        return "ReactionRole";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("reactionrole", "reactionroles", "rr");
    }

    @Override
    public String getDescription()
    {
        return "Controls reactionroles.";
    }

    @Override
    public String getSyntax()
    {
        return "[add|remove|clear] [messageID] [channel] [role] [reaction]";
    }

    @Override
    public boolean canExecute(CommandContext ctx)
    {
        return ctx.hasPermission(Collections.singletonList(Permission.ADMINISTRATOR));
    }

    @Override
    public boolean isGuildOnly()
    {
        return true;
    }

    @Override
    public long getCooldown()
    {
        return 0;
    }
}

