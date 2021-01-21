package org.igsq.igsqbot.commands.subcommands.level;

import java.util.List;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.exception.CommandException;
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
    public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
    {
        if(CommandChecks.argsEmpty(cmd, failure)) return;

        new Parser(args.get(0), cmd).parseAsUser(
        user ->
        {
            new GuildConfig(cmd).setLevelUpBot(user.getIdLong());
            cmd.replySuccess("New level up bot is " + user.getAsMention());
        });
    }
}
