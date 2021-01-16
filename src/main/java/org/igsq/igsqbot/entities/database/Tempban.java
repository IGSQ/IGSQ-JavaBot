package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.Mutes;
import org.igsq.igsqbot.entities.jooq.tables.Roles;

public class Tempban
{
	private final LocalDateTime mutedUntil;
	private final IGSQBot igsqBot;
	private final long memberId;
	private final Guild guild;
	private final List<Long> roleIds;

	public Tempban(long memberId, List<Long> roleIds, Guild guild, LocalDateTime mutedUntil, IGSQBot igsqBot)
	{
		this.memberId = memberId;
		this.roleIds = roleIds;
		this.guild = guild;
		this.mutedUntil = mutedUntil;
		this.igsqBot = igsqBot;
	}

	public static void removeMuteById(long userId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var ctx = igsqBot.getDatabaseManager().getContext(connection);
			var roles = ctx.selectFrom(Tables.ROLES).where(Roles.ROLES.USERID.eq(userId));
			List<Role> collectedRoles = new ArrayList<>();
			Guild guild = null;
			for(var row : roles.fetch())
			{
				guild = igsqBot.getShardManager().getGuildById(row.getGuildid());
				if(guild != null)
				{
					Role role = guild.getRoleById(row.getRoleid());
					if(role != null)
					{
						if(!collectedRoles.contains(role))
						{
							collectedRoles.add(role);
						}
					}
				}
			}

			if(guild != null)
			{
				Guild finalGuild = guild;
				guild.retrieveMemberById(userId).queue(member -> finalGuild.modifyMemberRoles(member, collectedRoles).queue());
			}
			ctx.deleteFrom(Tables.MUTES).where(Mutes.MUTES.USERID.eq(userId)).execute();
			ctx.deleteFrom(Tables.ROLES).where(Roles.ROLES.USERID.eq(userId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public boolean add()
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var ctx = igsqBot.getDatabaseManager().getContext(connection);

			boolean exists = ctx.select(Mutes.MUTES.USERID).from(Tables.MUTES).fetchOne() != null;
			if(exists)
			{
				return false;
			}

			for(long roleId : roleIds)
			{
				ctx.insertInto(Tables.ROLES).columns(Roles.ROLES.GUILDID, Roles.ROLES.USERID, Roles.ROLES.ROLEID).values(guild.getIdLong(), memberId, roleId).execute();
			}
			ctx.insertInto(Tables.MUTES).columns(Mutes.MUTES.GUILDID, Mutes.MUTES.USERID, Mutes.MUTES.MUTEDUNTIL).values(guild.getIdLong(), memberId, mutedUntil).execute();

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
			var ctx = igsqBot.getDatabaseManager().getContext(connection);
			ctx.deleteFrom(Tables.MUTES).where(Mutes.MUTES.USERID.eq(memberId)).execute();
			ctx.deleteFrom(Tables.ROLES).where(Roles.ROLES.USERID.eq(memberId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
