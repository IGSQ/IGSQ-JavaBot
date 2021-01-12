package org.igsq.igsqbot.minecraft;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.ConfigOption;
import org.igsq.igsqbot.entities.Configuration;

import java.sql.Connection;

public class DatabaseHandler
{
	private final IGSQBot igsqBot;
	private final HikariDataSource pool;

	public DatabaseHandler(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		this.pool = initHikari();
	}

	public HikariDataSource getPool()
	{
		return pool;
	}

	public Connection getConnection()
	{
		try
		{
			return pool.getConnection();
		}
		catch(Exception exception)
		{
			return getConnection();
		}
	}

	public boolean isOnline()
	{
		return pool.isRunning();
	}

	private HikariDataSource initHikari()
	{
		HikariConfig hikariConfig = new HikariConfig();
		Configuration configuration = igsqBot.getConfig();
		hikariConfig.setDriverClassName(configuration.getString(ConfigOption.REMOTEDRIVER));
		hikariConfig.setJdbcUrl(configuration.getString(ConfigOption.REMOTEURL));

		hikariConfig.setUsername(igsqBot.getConfig().getString(ConfigOption.REMOTEUSERNAME));
		hikariConfig.setPassword(igsqBot.getConfig().getString(ConfigOption.REMOTEPASSWORD));

		hikariConfig.setMaximumPoolSize(30);
		hikariConfig.setMinimumIdle(10);
		hikariConfig.setConnectionTimeout(10000);
		return new HikariDataSource(hikariConfig);
	}

	public void close()
	{
		pool.close();
	}
}
