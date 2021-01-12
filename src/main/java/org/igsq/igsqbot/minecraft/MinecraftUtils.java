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

	public static void removeCode(String userId, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("UPDATE discord_2fa SET code = null, current_status = ? WHERE uuid = ?");
			TwoFactorState twoFactorState = getTwoFAStatus(userId, minecraft);

			if(twoFactorState == TwoFactorState.ACCEPTED)
			{
				statement.setString(1, "accepted");
			}
			else
			{
				statement.setString(1, "expired");
			}
			statement.setString(2, getUUID(userId, minecraft));
			statement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static void addCode(String userId, String code, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("UPDATE discord_2fa SET code = ? WHERE uuid = ?");
			statement.setString(1, code);
			statement.setString(2, getUUID(userId, minecraft));
			statement.executeUpdate();
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
		}
	}

	public static String getUserId(String uuid, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT id FROM linked_accounts WHERE uuid = ?");
			statement.setString(1, uuid);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			return resultSet.getString(1);
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return null;
		}
	}

	public static String getUUID(String id, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM linked_accounts WHERE id = ?");
			statement.setString(1, id);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			return resultSet.getString(1);
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return null;
		}
	}

	public static TwoFactorState getTwoFAStatus(String userId, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT current_status FROM discord_2fa WHERE uuid = ?");
			statement.setString(1, getUUID(userId, minecraft));
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			return switch(resultSet.getString(1).toUpperCase())
					{
						case "ACCEPTED" -> TwoFactorState.valueOf("ACCEPTED");
						case "EXPIRED" -> TwoFactorState.valueOf("EXPIRED");
						case "PENDING" -> TwoFactorState.valueOf("PENDING");
						default -> TwoFactorState.valueOf("UNKNOWN");
					};
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return null;
		}
	}

	public static LinkState getLinkStatus(String userId, Minecraft minecraft)
	{
		try(Connection connection = minecraft.getDatabaseHandler().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT current_status FROM linked_accounts WHERE id = ?");
			statement.setString(1, userId);
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			resultSet.next();
			return switch(resultSet.getString(1).toUpperCase())
					{
						case "MWAIT" -> LinkState.valueOf("MWAIT");
						case "DWAIT" -> LinkState.valueOf("DWAIT");
						case "LINKED" -> LinkState.valueOf("LINKED");
						default -> LinkState.valueOf("UNKNOWN");
					};
		}
		catch(Exception exception)
		{
			minecraft.getIGSQBot().getLogger().error("An SQL error has occurred", exception);
			return null;
		}
	}

	public enum TwoFactorState
	{
		ACCEPTED,
		EXPIRED,
		PENDING,
		UNKNOWN
	}

	public enum LinkState
	{
		MWAIT,
		DWAIT,
		LINKED,
		UNKNOWN
	}
}
