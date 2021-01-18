package org.igsq.igsqbot.commands.subcommands.level;

import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Level;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Levels;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.StringUtils;

public class LevelShowCommand extends Command
{
    public LevelShowCommand(Command parent)
    {
        super(parent, "show", "Shows all levels.", "[none]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        User levelBot = ctx.getIGSQBot().getShardManager().getUserById(new GuildConfig(ctx).getLevelUpBot());
        CommandChecks.userConfigured(levelBot, "Level up bot");
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
                .append(level.getAwardedAt());
        }

        ctx.sendMessage(new EmbedBuilder()
            .setTitle("Configured levels for " + ctx.getGuild().getName())
            .setDescription(text.length() == 0 ? "No levels configured" : text.toString())
            .setColor(Constants.IGSQ_PURPLE));
    }
}
