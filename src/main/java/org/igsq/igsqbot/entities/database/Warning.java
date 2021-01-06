package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.tables.Warnings;

import java.sql.Connection;
import java.time.LocalDateTime;
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
					.insertInto(Warnings.WARNINGS)
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
					.deleteFrom(Warnings.WARNINGS)
					.where(Warnings.WARNINGS.WARNID.eq(key));
			context.execute();
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public List<Warn> get()
	{
		List<Warn> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.selectFrom(Warnings.WARNINGS)
					.where(Warnings.WARNINGS.GUILDID.eq(guildId))
					.and(Warnings.WARNINGS.USERID.eq(userId));

			for(var value : context.fetch())
			{
				result.add(new Warn(value.getWarnid(), value.getUserid(), value.getGuildid(), value.getTimestamp(), value.getWarntext()));
			}
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}

		return result;
	}

	public Warn getById(long warnId)
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
				return new Warn(warn.getWarnid(), warn.getUserid(), warn.getGuildid(), warn.getTimestamp(), warn.getWarntext());
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

	public static class Warn
	{
		private final long warnId;
		private final long userId;
		private final LocalDateTime timeStamp;
		private final String warnText;
		private final long guildId;

		public long getWarnId()
		{
			return warnId;
		}

		public long getUserId()
		{
			return userId;
		}

		public long getGuildId()
		{
			return guildId;
		}

		public LocalDateTime getTimeStamp()
		{
			return timeStamp;
		}

		public String getWarnText()
		{
			return warnText;
		}

		public Warn(long warnId, long userId, long guildId, LocalDateTime timeStamp, String warnText)
		{
			this.warnId = warnId;
			this.userId = userId;
			this.guildId = guildId;
			this.timeStamp = timeStamp;
			this.warnText = warnText;
		}
	}
}
