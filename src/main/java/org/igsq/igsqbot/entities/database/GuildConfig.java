package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.CommandContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
			PreparedStatement statement = connection.prepareStatement("SELECT prefix FROM guilds WHERE guildId = ?");
			statement.setLong(1, guildId);
			if(statement.execute())
			{
				ResultSet resultSet = statement.getResultSet();
				if(resultSet.next())
				{
					return resultSet.getString(1);
				}
			}
			return ".";
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
			PreparedStatement statement = connection.prepareStatement("UPDATE guilds SET prefix = ?");
			statement.setString(1, prefix);
			statement.executeUpdate();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public long getReportChannel()
	{
		return getValue("reportChannel");
	}

	public long getLogChannel()
	{
		return getValue("logChannel");
	}

	private long getValue(String field)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT ? FROM guilds WHERE guildId = ?");
			statement.setString(1, field);
			statement.setLong(2, guildId);

			if(statement.execute())
			{
				ResultSet resultSet = statement.getResultSet();
				if(resultSet.next())
				{
					return resultSet.getLong(1);
				}
			}
			return -1;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}

	public long getVoteChannel()
	{
		return getValue("voteChannel");
	}

	public long getMutedRole()
	{
		return getValue("mutedRole");
	}
}
