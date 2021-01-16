package org.igsq.igsqbot.commands.misc;

import java.util.Arrays;
import java.util.List;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

public class PingCommand extends Command
{
    @Override
    public void execute(List<String> args, CommandContext ctx)
    {

    }

    @Override
    public String getName()
    {
        return "Ping";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("ping", "latency");
    }

    @Override
    public String getDescription()
    {
        return "Shows the bots current ping to Discord";
    }

    @Override
    public String getSyntax()
    {
        return "[none]";
    }

    @Override
    public boolean canExecute(CommandContext ctx)
    {
        return true;
    }

    @Override
    public boolean isGuildOnly()
    {
        return false;
    }

    @Override
    public long getCooldown()
    {
        return 0;
    }
}
