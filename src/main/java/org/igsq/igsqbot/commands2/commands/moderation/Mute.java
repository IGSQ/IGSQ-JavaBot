package org.igsq.igsqbot.commands2.commands.moderation;

import java.util.List;

import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;

public class Mute extends NewCommand
{
    public Mute()
    {
        super("Tempban", "Temporarily bans a user.", "[user][duration]");
        addAliases("mute", "tempban");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {

    }
}
