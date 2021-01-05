package org.igsq.igsqbot.entities.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.igsq.igsqbot.IGSQBot;

import java.sql.Connection;

public class DatabaseManager
{
	private final IGSQBot igsqBot;
	private final HikariDataSource pool;

	public DatabaseManager(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("org.postgresql.Driver");
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/igsqbot");
		config.setUsername("igsqbot");
		config.setPassword("HSP23En5ERLenMqxioZ3");

		config.setMaximumPoolSize(30);
		config.setMinimumIdle(10);
		config.setConnectionTimeout(10000);

		this.pool = new HikariDataSource(config);
	}

	public void createTables()
	{
		Connection connection = getConnection();
		try
		{
			connection.createStatement().execute("");
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An error occurred while setting up the Database.", exception);
			System.exit(-1);
		}

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

	public void dropConnection(Connection connection)
	{
		pool.evictConnection(connection);
	}
}
