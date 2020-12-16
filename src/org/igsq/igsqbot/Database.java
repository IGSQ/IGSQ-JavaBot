package org.igsq.igsqbot;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class Database 
{
    private Database()
    {

    }

    private static String url;
    private static String user;
    private static String password;
	public static void startDatabase()
    {
        url = Yaml.getFieldString("mysql.database", "config");
        user = Yaml.getFieldString("mysql.username", "config");
        password = Yaml.getFieldString("mysql.password", "config");
        if(!testDatabase())
        {
            System.out.println("A database connection could not be established, " +
                    "this could be due to a faulty installation, " +
                    "incorrect credentials, or a networking error");
        }
    }

	public static ResultSet queryCommand(String sql)
	{
        try 
        {
        	Connection connection = DriverManager.getConnection(url, user, password);
            Statement commandAdapter = connection.createStatement();
            ResultSet resultTable = commandAdapter.executeQuery(sql);
            Common.scheduler.schedule(() -> {
                try
                {
                    connection.close();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }
            },3, TimeUnit.SECONDS);
			return resultTable;
        }
        catch (SQLException exception) 
        {
        	return null;
        } 
    }
	public static void updateCommand(String sql)
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
        	exception.printStackTrace();
        }
    }
	public static int scalarCommand(String sql)
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
            Connection connection =  DriverManager.getConnection(url, user, password);
            Statement commandAdapter = connection.createStatement();
            commandAdapter.executeUpdate("CREATE TABLE IF NOT EXISTS test_database(number int PRIMARY KEY AUTO_INCREMENT,test VARCHAR(36));");
            commandAdapter.executeUpdate("DROP TABLE test_database;");
            connection.close();
            return true;
        }
        catch (Exception exception)
        {
            return false;
        }
    }
}
