package org.igsq.igsqbot.commands2.commands.misc;

import java.util.List;
import net.dv8tion.jda.api.entities.Icon;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.FileUtils;
import org.igsq.igsqbot.util.StringUtils;

public class StealCommand extends NewCommand
{
    public StealCommand()
    {
        super("Steal", "Steals the specified image URL and adds it as an emoji.", "[name{A-Z + _}] [imageURL]");
        addAliases("steal");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        if (args.size() < 2 || !StringUtils.isURLValid(args.get(1)) || !args.get(0).matches("([A-Z]|[a-z]|_)\\w+"))
        {
            EmbedUtils.sendSyntaxError(ctx);
        }
        else
        {
            Icon icon = FileUtils.getIcon(args.get(1));
            if (icon == null)
            {
                ctx.replyError("The image / gif provided could not be loaded.");
            }
            else
            {
                ctx.getGuild().createEmote(args.get(0), icon).queue(
                        emote -> ctx.replySuccess("Added emote " + emote.getAsMention() + " successfully!"),
                        error -> ctx.replyError("An error occurred while adding the emote."));
            }
        }
    }
}