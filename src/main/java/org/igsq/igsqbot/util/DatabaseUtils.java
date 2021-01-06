package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.igsq.igsqbot.IGSQBot;

import java.sql.Connection;

import static org.igsq.igsqbot.entities.jooq.tables.Guilds.GUILDS;
import static org.igsq.igsqbot.entities.jooq.tables.Users.USERS;
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
					.insertInto(GUILDS)
					.columns(GUILDS.GUILDID)
					.values(guild.getIdLong())
					.onDuplicateKeyIgnore();
			context.execute();
			context.close();
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
					.insertInto(GUILDS)
					.columns(GUILDS.GUILDID)
					.values(guildId)
					.onDuplicateKeyIgnore();
			context.execute();
			context.close();
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
					.insertInto(USERS)
					.columns(USERS.USERID, USERS.GUILDID)
					.values(member.getIdLong(), member.getGuild().getIdLong())
					.onDuplicateKeyIgnore();
			context.execute();
			context.close();
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
					.insertInto(USERS)
					.columns(USERS.USERID, USERS.GUILDID)
					.values(userId, guildId)
					.onDuplicateKeyIgnore();
			context.execute();
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
