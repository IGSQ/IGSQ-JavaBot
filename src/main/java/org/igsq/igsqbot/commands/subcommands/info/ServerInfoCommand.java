package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.info.GuildInfo;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

public class ServerInfoCommand extends Command
{
	public ServerInfoCommand(Command parent)
	{
		super(parent, "server", "Shows information about a server", "[server]");
		guildOnly();
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
