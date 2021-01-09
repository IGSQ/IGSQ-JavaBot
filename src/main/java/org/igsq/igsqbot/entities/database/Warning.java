package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.Warnings;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Warning
{
	private final long userId;
	private final long guildId;
	private final IGSQBot igsqBot;

	public Warning(long guildId, long userId, IGSQBot igsqBot)
	{
		this.guildId = guildId;
		this.userId = userId;
		this.igsqBot = igsqBot;
	}

	public Warning(Guild guild, User user, IGSQBot igsqBot)
	{
		this.guildId = guild.getIdLong();
		this.userId = user.getIdLong();
		this.igsqBot = igsqBot;
	}
	
	public void add(String reason)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.WARNINGS)
					.columns(Warnings.WARNINGS.GUILDID, Warnings.WARNINGS.USERID, Warnings.WARNINGS.WARNTEXT)
					.values(guildId, userId, reason);
			context.execute();
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public void remove(long key)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.deleteFrom(Tables.WARNINGS)
					.where(Warnings.WARNINGS.WARNID.eq(key));
			context.execute();
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public List<org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings> get()
	{
		List<org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.selectFrom(Warnings.WARNINGS)
					.where(Warnings.WARNINGS.GUILDID.eq(guildId))
					.and(Warnings.WARNINGS.USERID.eq(userId));

			for(var value : context.fetch())
			{
				result.add(new org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings(value.getWarnid(), value.getUserid(), value.getGuildid(), value.getTimestamp(), value.getWarntext()));
			}
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}

		return result;
	}

	public org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings getById(long warnId)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.selectFrom(Warnings.WARNINGS)
					.where(Warnings.WARNINGS.WARNID.eq(warnId));

			var result = context.fetch();
			context.close();
			if(!result.isEmpty())
			{
				var warn = result.get(0);
				return new org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings(warn.getWarnid(), warn.getUserid(), warn.getGuildid(), warn.getTimestamp(), warn.getWarntext());
			}
			else
			{
				return null;
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}
}
