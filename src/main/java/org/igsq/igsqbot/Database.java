package org.igsq.igsqbot;

import org.igsq.igsqbot.handlers.TaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class Database
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);
	private static String url;
	private static String user;
	private static String password;
	private static boolean isOnline;

	private Database()
	{
		//Overrides the default, public, constructor
	}

	public static void startDatabase()
	{
		url = Yaml.getFieldString("mysql.database", "config");
		user = Yaml.getFieldString("mysql.username", "config");
		password = Yaml.getFieldString("mysql.password", "config");
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

	public static ResultSet queryCommand(String sql)
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

	public static void updateCommand(String sql)
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

	public static int scalarCommand(String sql)
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

	public static Boolean testDatabase()
	{
		try
		{
			Connection connection = DriverManager.getConnection(url, user, password);
			Statement commandAdapter = connection.createStatement();
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
}
