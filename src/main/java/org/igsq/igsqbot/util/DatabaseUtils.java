package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.Guilds;
import org.igsq.igsqbot.entities.jooq.tables.Users;

import java.sql.Connection;

public class DatabaseUtils
{
	private DatabaseUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void registerGuild(Guild guild, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(Guilds.GUILDS.GUILDID)
					.values(guild.getIdLong())
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerGuild(long guildId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.GUILDS)
					.columns(Guilds.GUILDS.GUILDID)
					.values(guildId)
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerUser(Member member, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.USERS)
					.columns(Users.USERS.USERID, Users.USERS.GUILDID)
					.values(member.getIdLong(), member.getGuild().getIdLong())
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void registerUser(long userId, long guildId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.USERS)
					.columns(Users.USERS.USERID, Users.USERS.GUILDID)
					.values(userId, guildId)
					.onDuplicateKeyIgnore();
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
