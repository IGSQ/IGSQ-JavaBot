package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.CommandContext;
import org.jooq.Field;

import java.sql.Connection;

import static org.igsq.igsqbot.entities.jooq.tables.Guilds.GUILDS;

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

	public GuildConfig(Guild guild, IGSQBot igsqBot)
	{
		this.guildId = guild.getIdLong();
		this.igsqBot = igsqBot;
	}

	public String getPrefix()
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.select(GUILDS.PREFIX)
					.from(GUILDS)
					.where(GUILDS.GUILDID.eq(guildId));

			String result = context.fetchOne(GUILDS.PREFIX);
			context.close();
			return result;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return ".";
		}
	}

	public void setPrefix(String prefix)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.update(GUILDS)
					.set(GUILDS.PREFIX, prefix)
					.where(GUILDS.GUILDID.eq(guildId));

			context.execute();
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public long getReportChannel()
	{
		return getValue(GUILDS.REPORTCHANNEL);
	}

	public long getLogChannel()
	{
		return getValue(GUILDS.LOGCHANNEL);
	}

	private long getValue(Field<?> value)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.select(value)
					.from(GUILDS)
					.where(GUILDS.GUILDID.eq(guildId));

			long result = Long.parseLong(String.valueOf(context.fetchOne(value)));
			context.close();
			return result;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}

	public long getVoteChannel()
	{
		return getValue(GUILDS.VOTECHANNEL);
	}

	public long getMutedRole()
	{
		return getValue(GUILDS.MUTEDROLE);
	}
}
