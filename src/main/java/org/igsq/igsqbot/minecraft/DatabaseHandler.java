package org.igsq.igsqbot.minecraft;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.Config;
import org.igsq.igsqbot.entities.ConfigOption;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;

public class DatabaseHandler
{
	private final IGSQBot igsqBot;
	private final HikariDataSource pool;

	public DatabaseHandler(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		this.pool = initHikari();

		System.getProperties().setProperty("org.jooq.no-logo", "true");
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
		Config config = igsqBot.getConfig();
		hikariConfig.setDriverClassName(config.getOption(ConfigOption.REMOTEDRIVER));
		hikariConfig.setJdbcUrl("jdbc:" +
				config.getOption(ConfigOption.REMOTETYPE) + "://" +
				config.getOption(ConfigOption.REMOTEADDRESS) + ":" +
				config.getOption(ConfigOption.REMOTEPORT) + "/" +
				config.getOption(ConfigOption.REMOTEDATABASE));

		hikariConfig.setUsername(igsqBot.getConfig().getOption(ConfigOption.REMOTEUSERNAME));
		hikariConfig.setPassword(igsqBot.getConfig().getOption(ConfigOption.REMOTEPASSWORD));

		hikariConfig.setMaximumPoolSize(30);
		hikariConfig.setMinimumIdle(10);
		hikariConfig.setConnectionTimeout(10000);
		return new HikariDataSource(hikariConfig);
	}

	public DSLContext getContext()
	{
		return getContext(getConnection());
	}

	public DSLContext getContext(Connection connection)
	{
		return DSL.using(connection, SQLDialect.MYSQL);
	}

	public void close()
	{
		pool.close();
	}
}
