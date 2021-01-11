package org.igsq.igsqbot.minecraft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MinecraftUtils
{
	private MinecraftUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void insertMember(MinecraftUser user, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement preparedStatement = connection.prepareStatement("" +
					"INSERT INTO discord_accounts (id, username, nickname, role, founder, birthday, nitroboost, supporter, developer) " +
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

			fillStatement(user, preparedStatement);
			preparedStatement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static void updateMember(MinecraftUser user, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE discord_accounts SET " +
					"id = ?," +
					"username = ?," +
					"nickname = ?," +
					"role = ?," +
					"founder = ?," +
					"birthday = ?," +
					"nitroboost = ?," +
					"supporter = ?," +
					"developer = ? " +
					"WHERE id = ?");

			fillStatement(user, preparedStatement);
			preparedStatement.setString(10, user.getId());
			preparedStatement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	private static void fillStatement(MinecraftUser user, PreparedStatement preparedStatement) throws SQLException
	{
		preparedStatement.setString(1, user.getId());
		preparedStatement.setString(2, user.getUsername());
		preparedStatement.setString(3, user.getNickname());
		preparedStatement.setString(4, user.getRole());

		preparedStatement.setInt(5, user.getFounder());
		preparedStatement.setInt(6, user.getBirthday());
		preparedStatement.setInt(7, user.getNitroboost());
		preparedStatement.setInt(8, user.getSupporter());
		preparedStatement.setInt(9, user.getDeveloper());
	}

	public static int isMemberPresent(MinecraftUser user, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM discord_accounts WHERE id = ?");

			preparedStatement.setString(1, user.getId());
			preparedStatement.executeQuery();

			ResultSet resultSet = preparedStatement.getResultSet();
			resultSet.next();
			return resultSet.getInt(1);
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return -1;
		}
	}
}
