package org.igsq.igsqbot.commands2.subcommands.info;

import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.entities.info.GuildInfo;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

public class ServerInfoCommand extends NewCommand
{
    public ServerInfoCommand(NewCommand parent)
    {
        super(parent, "server", "Shows information about a server", "[server]");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        if(args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(ctx);
        }
        else
        {
            Guild guild = new Parser(args.get(1), ctx).parseAsGuild();

            if(guild != null)
            {
                GuildInfo guildInfo = new GuildInfo(guild);
            }
        }
    }
}
