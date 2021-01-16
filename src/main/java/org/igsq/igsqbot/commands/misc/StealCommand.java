package org.igsq.igsqbot.commands.misc;

import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

public class StealCommand extends Command
{
    @Override
    public void execute(List<String> args, CommandContext ctx)
    {

    }

    @Override
    public String getName()
    {
        return "Steal";
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList("steal");
    }

    @Override
    public String getDescription()
    {
        return "Steals the specified image URL and adds it as an emoji.";
    }

    @Override
    public String getSyntax()
    {
        return "[name{A-Z + _}] [imageURL]";
    }

    @Override
    public boolean canExecute(CommandContext ctx)
    {
        return ctx.hasPermission(Collections.singletonList(Permission.MANAGE_EMOTES));
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
