package org.igsq.igsqbot.commands.subcommands.level;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Level;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Levels;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.StringUtils;

public class LevelShowCommand extends Command
{
    public LevelShowCommand(Command parent)
    {
        super(parent, "show", "Shows information about the levels.", "[none]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
    {
        User levelBot = ctx.getIGSQBot().getShardManager().getUserById(new GuildConfig(ctx).getLevelUpBot());
        if(CommandChecks.userConfigured(levelBot, "Level up bot", failure)) return;
        List<Levels> levelList = Level.showLevels(ctx.getGuildIdLong(), ctx.getIGSQBot());

        StringBuilder text = new StringBuilder();

        for(Levels level : levelList)
        {
            text
                .append("Role ")
                .append(StringUtils.getRoleAsMention(level.getRoleId()))
                .append(" awarded at ")
                .append(levelBot.getAsMention())
                .append(" level ")
                .append(level.getAwardedAt())
                .append("\n");
        }

        ctx.sendMessage(new EmbedBuilder()
            .setTitle("Configured levels for " + ctx.getGuild().getName())
            .setDescription(text.length() == 0 ? "No levels configured" : text.toString()));
    }
}
