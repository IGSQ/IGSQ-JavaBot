package org.igsq.igsqbot.util;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.Guilds;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Mutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);
	private DatabaseUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void removeGuild(Guild guild, IGSQBot igsqBot)
	{
		LOGGER.debug("Removed guild " + guild.getId());
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
					.deleteFrom(Tables.GUILDS)
					.where(Guilds.GUILDS.GUILDID.eq(guild.getIdLong()));
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void removeGuild(long guildId, IGSQBot igsqBot)
	{
		LOGGER.debug("Removed guild " + guildId);
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
					.deleteFrom(Tables.GUILDS)
					.where(Guilds.GUILDS.GUILDID.eq(guildId));
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerGuild(Guild guild, IGSQBot igsqBot)
	{
		LOGGER.debug("Registered guild " + guild.getId());
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
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
		LOGGER.debug("Removed guild " + guildId);
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection)
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

	public static List<Mutes> getExpiredMutes(IGSQBot igsqBot)
	{
		List<Mutes> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection).selectFrom(Tables.MUTES);

			for(var value : context.fetch())
			{
				if(value.getMuteduntil().isBefore(LocalDateTime.now()))
				{
					result.add(new Mutes(value.getId(), value.getUserid(), value.getGuildid(), value.getMuteduntil()));
				}
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}
}
