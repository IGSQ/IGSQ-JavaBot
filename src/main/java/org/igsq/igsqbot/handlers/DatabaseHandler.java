package org.igsq.igsqbot.handlers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.util.FileUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.InputStream;
import java.sql.Connection;

public class DatabaseHandler
{
	private final IGSQBot igsqBot;
	private final HikariDataSource pool;

	public DatabaseHandler(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		this.pool = initHikari();

		initTables();
		System.getProperties().setProperty("org.jooq.no-logo", "true");
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

	private void initTables()
	{
		initTable("guilds");
		initTable("users");

		initTable("roles");
		initTable("mutes");
		initTable("reaction_roles");
		initTable("reminders");
		initTable("reports");
		initTable("warnings");
		initTable("votes");
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

	private void initTable(String table){
		try
		{
			InputStream file = DatabaseHandler.class.getClassLoader().getResourceAsStream("sql/" + table + ".sql");
			if(file == null)
			{
				throw new NullPointerException("File for table '" + table + "' not found");
			}
			getConnection().createStatement().execute(FileUtils.convertToString(file));
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("Error initializing table: '" + table + "'", exception);
		}
	}

	private HikariDataSource initHikari()
	{
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("org.postgresql.Driver");
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/igsqbot");
		config.setUsername("igsqbot");
		config.setPassword("HSP23En5ERLenMqxioZ3");

		config.setMaximumPoolSize(30);
		config.setMinimumIdle(10);
		config.setConnectionTimeout(10000);
		return new HikariDataSource(config);
	}

	public DSLContext getContext()
	{
		return getContext(getConnection());
	}

	public DSLContext getContext(Connection connection)
	{
		return DSL.using(connection, SQLDialect.POSTGRES);
	}

	public void dropConnection(Connection connection)
	{
		pool.evictConnection(connection);
	}
}
