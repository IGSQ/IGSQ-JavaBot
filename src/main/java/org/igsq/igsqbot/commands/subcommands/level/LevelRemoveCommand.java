package org.igsq.igsqbot.commands.subcommands.level;

import java.util.List;

import java.util.OptionalInt;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Level;
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
    public void run(List<String> args, CommandContext ctx)
    {
        User levelBot = ctx.getIGSQBot().getShardManager().getUserById(new GuildConfig(ctx).getLevelUpBot());
        CommandChecks.userConfigured(levelBot, "Level up bot");
        CommandChecks.argsSizeSubceeds(ctx, 2);
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
                            throw new CommandResultException("Level " + level.getAsInt() + " with role " + role.getAsMention() + " was not found.");
                        }
                    });
        }
    }
}
