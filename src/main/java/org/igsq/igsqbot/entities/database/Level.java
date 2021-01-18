package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Levels;

import static org.igsq.igsqbot.entities.jooq.tables.Levels.LEVELS;

public class Level
{
	private Level()
	{
		//Overrides the default, public, constructor
	}

	public static void addLevel(Role role, int level, long guildId,IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(Tables.LEVELS)
					.columns(LEVELS.GUILD_ID, LEVELS.ROLE_ID, LEVELS.AWARDED_AT)
					.values(guildId, role.getIdLong(), level);

			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static boolean removeLevel(Role role, int level, long guildId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(Tables.LEVELS)
					.where(LEVELS.GUILD_ID.eq(guildId)
					.and(LEVELS.ROLE_ID.eq(role.getIdLong()))
					.and(LEVELS.AWARDED_AT.eq(level)));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}

	}

	public static List<Levels> showLevels(long guildId, IGSQBot igsqBot)
	{
		List<Levels> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.LEVELS)
					.where(LEVELS.GUILD_ID.eq(guildId));

			for(var row : query.fetch())
			{
				result.add(new Levels(row.getId(), row.getGuildId(), row.getRoleId(), row.getAwardedAt()));
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}

	public void setBot(User bot, long guildId, IGSQBot igsqBot)
	{
		new GuildConfig(guildId, igsqBot).setLevelUpBot(bot.getIdLong());
	}
}
