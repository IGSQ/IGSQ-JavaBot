package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.jooq.tables.Guilds;

import java.sql.Connection;

public class GuildConfig
{
	private final IGSQBot igsqBot;
	private final long guildId;

	public GuildConfig(Guild guild, CommandContext ctx)
	{
		this.guildId = guild.getIdLong();
		this.igsqBot = ctx.getIGSQBot();
	}

	public GuildConfig(long guildId, IGSQBot igsqBot)
	{
		this.guildId = guildId;
		this.igsqBot = igsqBot;
	}

	public String getPrefix()
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.select(Guilds.GUILDS.PREFIX)
					.from(Guilds.GUILDS)
					.where(Guilds.GUILDS.GUILDID.eq(guildId));

			return context.fetchOne(Guilds.GUILDS.PREFIX);
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return ".";
		}
	}
}
