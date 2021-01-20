package org.igsq.igsqbot.commands.subcommands.level;

import java.util.List;

import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Level;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;

public class LevelRemoveCommand extends Command
{
    public LevelRemoveCommand(Command parent)
    {
        super(parent, "remove", "Removes a level entry.", "[level][role]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
    {
        User levelBot = ctx.getIGSQBot().getShardManager().getUserById(new GuildConfig(ctx).getLevelUpBot());
        if(CommandChecks.userConfigured(levelBot, "Level up bot", failure)) return;
        if(CommandChecks.argsSizeSubceeds(ctx, 2, failure)) return;
        OptionalInt level = new Parser(args.get(0), ctx).parseAsUnsignedInt();
        if(level.isPresent())
        {
            new Parser(args.get(1), ctx).parseAsRole(
                    role ->
                    {
                        if(Level.removeLevel(role, level.getAsInt(), ctx.getGuildIdLong(), ctx.getIGSQBot()))
                        {
                            ctx.replySuccess("Removed level " + level.getAsInt() + " which awarded " + role.getAsMention());
                        }
                        else
                        {
                            failure.accept(new CommandResultException("Level " + level.getAsInt() + " with role " + role.getAsMention() + " was not found."));
                        }
                    });
        }
    }
}
