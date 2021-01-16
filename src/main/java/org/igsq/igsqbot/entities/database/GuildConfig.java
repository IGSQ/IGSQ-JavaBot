package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.CommandContext;
import org.jooq.Field;

import static org.igsq.igsqbot.entities.jooq.tables.Guilds.GUILDS;

public class GuildConfig
{
	private final IGSQBot igsqBot;
	private final long guildId;

	public GuildConfig(CommandContext ctx)
	{
		this.guildId = ctx.getGuild().getIdLong();
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
			var context = igsqBot.getDatabaseManager().getContext(connection);
			var query = context.select(GUILDS.PREFIX).from(GUILDS).where(GUILDS.GUILDID.eq(guildId));
			String result = query.fetchOne(GUILDS.PREFIX);
			query.close();
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
			var context = igsqBot.getDatabaseManager().getContext(connection);
			var query = context.update(GUILDS).set(GUILDS.PREFIX, prefix).where(GUILDS.GUILDID.eq(guildId));
			query.execute();
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

	public long getVoteChannel()
	{
		return getValue(GUILDS.VOTECHANNEL);
	}

	public long getMutedRole()
	{
		return getValue(GUILDS.MUTEDROLE);
	}

	private long getValue(Field<?> value)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection);
			var query = context.select(value).from(GUILDS).where(GUILDS.GUILDID.eq(guildId));
			long result = Long.parseLong(String.valueOf(query.fetchOne(value)));
			query.close();
			return result;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}
}
