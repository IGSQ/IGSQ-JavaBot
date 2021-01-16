package org.igsq.igsqbot.commands.moderation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.util.EmbedUtils;

public class ClearCommand extends Command
{
    @Override
    public void execute(List<String> args, CommandContext ctx)
    {

    }

    @Override
    public String getName()
    {
        return "Clear";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("clear", "purge");
    }

    @Override
    public String getDescription()
    {
        return "Clears messages from the current channel";
    }

    @Override
    public String getSyntax()
    {
        return "[amount {50}]";
    }

    @Override
    public boolean canExecute(CommandContext ctx)
    {
        return ctx.hasPermission(Collections.singletonList(Permission.MESSAGE_MANAGE));
    }

    @Override
    public boolean isGuildOnly()
    {
        return true;
    }

    @Override
    public long getCooldown()
    {
        return 10000;
    }
}
