package org.igsq.igsqbot;

import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.JsonBotConfig;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Database
{
	private static final Database INSTANCE = new Database();
	private final Logger LOGGER = LoggerFactory.getLogger(Database.class);

	private String url;
	private String user;
	private String password;
	private boolean isOnline;

	private Database()
	{
		//Overrides the default, public, constructor
	}

	public void start()
	{
		JsonBotConfig jsonBotConfig = Json.get(JsonBotConfig.class, Filename.CONFIG);
		if(jsonBotConfig != null)
		{
			Map<String, String> credentials = jsonBotConfig.getSQL();
			url = credentials.get("url");
			user = credentials.get("username");
			password = credentials.get("password");
		}
		else
		{
			LOGGER.error("An error occurred while loading the Database credentials, the Database is unlikely to work.");
		}

		if(testDatabase())
		{
			isOnline = true;
		}
		else
		{
			LOGGER.error("A database connection could not be established, " +
					"this could be due to a faulty installation, " +
					"incorrect credentials, or a networking error. Database functionality disabled.");
			isOnline = false;
		}
	}

	public ResultSet queryCommand(String sql)
	{
		if(!isOnline)
		{
			return null;
		}
		try
		{
			Connection connection = DriverManager.getConnection(url, user, password);
			Statement commandAdapter = connection.createStatement();
			ResultSet resultTable = commandAdapter.executeQuery(sql);
			TaskHandler.addTask(() ->
			{
				try
				{
					connection.close();
				}
				catch(Exception exception)
				{
					LOGGER.error("A database error occurred.", exception);
				}
			}, TimeUnit.SECONDS, 3);
			return resultTable;
		}
		catch(Exception exception)
		{
			return null;
		}
	}

	public void updateCommand(String sql)
	{
		if(!isOnline)
		{
			return;
		}
		try
		{
			Connection connection = DriverManager.getConnection(url, user, password);
			Statement commandAdapter = connection.createStatement();
			commandAdapter.executeUpdate(sql);
			connection.close();
		}
		catch(Exception exception)
		{

		}
	}

	public int scalarCommand(String sql)
	{
		if(!isOnline)
		{
			return -1;
		}
		try
		{
			Connection connection = DriverManager.getConnection(url, user, password);
			Statement commandAdapter = connection.createStatement();
			ResultSet resultTable = commandAdapter.executeQuery(sql);
			resultTable.next();
			int result = resultTable.getInt(1);
			connection.close();
			return result;
		}
		catch(Exception exception)
		{
			return -1;
		}
	}

	public boolean testDatabase()
	{
		try
		{
			Connection connection = DriverManager.getConnection(url, user, password);
			Statement commandAdapter = connection.createStatement();
			commandAdapter.execute("SELECT * FROM discord_2fa");
			commandAdapter.execute("SELECT * FROM discord_accounts");
			commandAdapter.execute("SELECT * FROM linked_accounts");
			commandAdapter.execute("SELECT * FROM mc_accounts");

			commandAdapter.executeUpdate("CREATE TABLE IF NOT EXISTS test_database(number int PRIMARY KEY AUTO_INCREMENT,test VARCHAR(36));");
			commandAdapter.executeUpdate("DROP TABLE test_database;");
			connection.close();
			return true;
		}
		catch(Exception exception)
		{
			return false;
		}
	}

	public boolean isOnline()
	{
		return isOnline;
	}

	public static Database getInstance()
	{
		return INSTANCE;
	}
}
