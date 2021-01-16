package org.igsq.igsqbot.commands2.commands.misc;

import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;

public class SuggestionCommand extends NewCommand
{
    public SuggestionCommand()
    {
        super("Suggestion", "Suggests an idea in the designated suggestion channel.", "[topic]");
        addAliases("suggest", "suggestion", "idea");
        guildOnly();
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        User author = ctx.getAuthor();
        GuildConfig guildConfig = new GuildConfig(ctx);

        if(args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(ctx);
            return;
        }

        ctx.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("Suggestion:")
                .setDescription(ArrayUtils.arrayCompile(args, " "))
                .setColor(Constants.IGSQ_PURPLE)
                .setThumbnail(author.getAvatarUrl())
                .setFooter("Suggestion by: " + ctx.getMember().getEffectiveName() + author.getDiscriminator() + " | ")
                .setTimestamp(Instant.now())
                .build()).queue();
    }
}
