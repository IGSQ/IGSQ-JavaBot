package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.Guilds;

import java.sql.Connection;

public class DatabaseUtils
{
	private DatabaseUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void registerGuild(Guild guild, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(Guilds.GUILDS.GUILDID)
					.values(guild.getIdLong())
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerGuild(long guildId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(Guilds.GUILDS.GUILDID)
					.values(guildId)
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
