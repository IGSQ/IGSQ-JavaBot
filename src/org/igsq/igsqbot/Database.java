package org.igsq.igsqbot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class Database 
{
    static String url;
    static String user;
    static String password;
	public Database()
	{
		url = Yaml.getFieldString("MYSQL.database", "config");
		user = Yaml.getFieldString("MYSQL.username", "config");
		password = Yaml.getFieldString("MYSQL.password", "config");
		if(testDatabase()) 
		{
			
		}
		else System.err.println("A Database Error Has Occured On Startup.");
	}
	public static ResultSet QueryCommand(String sql) 
	{
        try 
        {
        	Connection connection = DriverManager.getConnection(url, user, password);
            Statement commandAdapter = connection.createStatement();
            ResultSet resultTable = commandAdapter.executeQuery(sql);
            Common.scheduler.schedule(new Runnable() 
            {
				public void run() 
				{
					try
					{
						connection.close();
					}
					catch (Exception exception)
					{
						
					}
				} 
            },3, TimeUnit.SECONDS);
			return resultTable;
        }
        catch (SQLException exception) 
        {
        	return null;
        } 
    }
	public static void UpdateCommand(String sql) 
	{
        try 
        {
        	Connection connection = DriverManager.getConnection(url, user, password);
            Statement commandAdapter = connection.createStatement();
            commandAdapter.executeUpdate(sql);
            connection.close();
        }
        catch (SQLException exception) 
        {
        	
        }
    }
	public static int ScalarCommand(String sql) 
	{
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
        catch (SQLException exception) 
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
        catch (SQLException exception) 
        {
        	return false;
        }
    }
}
