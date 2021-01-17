package org.igsq.igsqbot.handlers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.ConfigOption;
import org.igsq.igsqbot.entities.Configuration;
import org.igsq.igsqbot.util.FileUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

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

	private void initTables()
	{
		initTable("guilds");

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

	private void initTable(String table)
	{
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
		HikariConfig hikariConfig = new HikariConfig();
		Configuration configuration = igsqBot.getConfig();
		hikariConfig.setDriverClassName("org.postgresql.Driver");
		hikariConfig.setJdbcUrl(configuration.getString(ConfigOption.LOCALURL));

		hikariConfig.setUsername(igsqBot.getConfig().getString(ConfigOption.LOCALUSERNAME));
		hikariConfig.setPassword(igsqBot.getConfig().getString(ConfigOption.LOCALPASSWORD));

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
		return DSL.using(connection, SQLDialect.POSTGRES);
	}

	public void close()
	{
		pool.close();
	}
}
