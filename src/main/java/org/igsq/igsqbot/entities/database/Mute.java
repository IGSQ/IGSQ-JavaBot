package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

public class Mute
{
	private final Timestamp mutedUntil;
	private final IGSQBot igsqBot;
	private final long memberId;
	private final Guild guild;
	private final List<Long> roleIds;

	public Mute(long memberId, List<Long> roleIds, Guild guild, Timestamp mutedUntil, IGSQBot igsqBot)
	{
		this.memberId = memberId;
		this.roleIds = roleIds;
		this.guild = guild;
		this.mutedUntil = mutedUntil;
		this.igsqBot = igsqBot;
	}

	public boolean add()
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			PreparedStatement existCheck = connection.prepareStatement("SELECT * FROM mutes WHERE userid = ?");
			existCheck.setLong(1, memberId);
			if(!existCheck.execute())
			{
				return false;
			}

			for(long roleId : roleIds)
			{

				PreparedStatement insertRole = connection.prepareStatement("INSERT INTO roles (guildid, userid, roleid) VALUES (?, ?, ?)");
				insertRole.setLong(1, guild.getIdLong());
				insertRole.setLong(2, memberId);
				insertRole.setLong(3, roleId);
				insertRole.executeUpdate();
			}


			PreparedStatement insertMute = connection.prepareStatement("INSERT INTO mutes (guildid, userid, muteduntil) VALUES (?,?,?)");
			insertMute.setLong(1, guild.getIdLong());
			insertMute.setLong(2, memberId);
			insertMute.setTimestamp(3, mutedUntil);

		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
		return true;
	}

	public void remove()
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			PreparedStatement removeMute = connection.prepareStatement("DELETE FROM mutes WHERE userid = ?");
			removeMute.setLong(1, memberId);
			removeMute.executeUpdate();

			PreparedStatement removeRoles = connection.prepareStatement("DELETE FROM roles WHERE userid = ?");
			removeRoles.setLong(1, memberId);
			removeRoles.executeUpdate();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void removeMuteById(long userId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			PreparedStatement getRoles = connection.prepareStatement("SELECT * FROM roles WHERE userid = ?");
			getRoles.setLong(1, userId);
			if(getRoles.execute())
			{
				ResultSet roles = getRoles.getResultSet();

				while(roles.next())
				{
					Guild guild = igsqBot.getShardManager().getGuildById(roles.getLong(3));
					if(guild != null)
					{
						Role role = guild.getRoleById(roles.getLong(4));
						if(role != null)
						{
							guild.addRoleToMember(roles.getLong(2), role).queue();
						}
					}
				}
			}

			PreparedStatement removeMute = connection.prepareStatement("DELETE FROM mutes WHERE userid = ?");
			removeMute.setLong(1, userId);
			removeMute.executeUpdate();

			PreparedStatement removeRoles = connection.prepareStatement("DELETE FROM roles WHERE userid = ?");
			removeRoles.setLong(1, userId);
			removeRoles.executeUpdate();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
