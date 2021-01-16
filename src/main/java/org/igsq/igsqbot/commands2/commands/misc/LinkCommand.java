package org.igsq.igsqbot.commands2.commands.misc;

import java.util.List;
import org.igsq.igsqbot.commands2.subcommands.link.LinkAddCommand;
import org.igsq.igsqbot.commands2.subcommands.link.LinkRemoveCommand;
import org.igsq.igsqbot.commands2.subcommands.link.LinkShowCommand;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;

public class LinkCommand extends NewCommand
{
    public LinkCommand()
    {
        super("Link", "Controls Minecraft links.", "[add|remove][mcName] | [show][user]");
        addAliases("link", "mclink");
        addChildren(
                new LinkAddCommand(this),
                new LinkRemoveCommand(this),
                new LinkShowCommand(this));
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        this.getChildren().get(2).run(args, ctx);
    }
}
