package org.igsq.igsqbot.commands.subcommands.blacklist;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.util.BlacklistUtils;

public class BlacklistShowCommand extends Command
{
    public BlacklistShowCommand(Command parent)
    {
        super(parent, "show", "Shows all blacklisted phrases for this server.", "[none]");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        List<String> blacklist = BlacklistUtils.getBlacklistedPhrases(ctx.getGuild(), ctx.getIGSQBot());
        StringBuilder text = new StringBuilder();

        for(String word : blacklist)
        {
            text
                .append("||")
                .append(word)
                .append("||")
                .append("\n");
        }

        ctx.sendMessage(new EmbedBuilder()
                .setTitle("Blacklisted words for server " + ctx.getGuild().getName())
                .setDescription(text.length() == 0 ? "No blacklisted words setup." : text.toString()));
    }
}
