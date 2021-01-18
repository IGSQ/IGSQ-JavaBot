package org.igsq.igsqbot.commands.subcommands.level;

import java.util.List;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;

public class LevelBotCommand extends Command
{
    public LevelBotCommand(Command parent)
    {
        super(parent, "bot", "Sets the bot to listen for.", "[user]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        CommandChecks.argsEmpty(ctx);

        new Parser(args.get(0), ctx).parseAsUser(
        user ->
        {
            new GuildConfig(ctx).setLevelUpBot(user.getIdLong());
            ctx.replySuccess("New level up is " + user.getAsMention());
        });
    }
}
