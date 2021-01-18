package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;

import static org.igsq.igsqbot.entities.jooq.tables.Roles.ROLES;
import static org.igsq.igsqbot.entities.jooq.tables.Tempbans.TEMPBANS;

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

	public static void removeBanById(long userId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);
			var roles = ctx.selectFrom(Tables.ROLES).where(ROLES.USER_ID.eq(userId));
			List<Role> collectedRoles = new ArrayList<>();
			Guild guild = null;
			for(var row : roles.fetch())
			{
				guild = igsqBot.getShardManager().getGuildById(row.getGuildId());
				if(guild != null)
				{
					Role role = guild.getRoleById(row.getRoleId());
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
			ctx.deleteFrom(Tables.TEMPBANS).where(TEMPBANS.USERID.eq(userId)).execute();
			ctx.deleteFrom(Tables.ROLES).where(ROLES.USER_ID.eq(userId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public boolean add()
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);

			boolean exists = ctx.select(TEMPBANS.USERID).from(Tables.TEMPBANS).fetchOne() != null;
			if(exists)
			{
				return false;
			}

			for(long roleId : roleIds)
			{
				ctx.insertInto(Tables.ROLES).columns(ROLES.GUILD_ID, ROLES.USER_ID, ROLES.ROLE_ID).values(guild.getIdLong(), memberId, roleId).execute();
			}
			ctx.insertInto(Tables.TEMPBANS).columns(TEMPBANS.GUILDID, TEMPBANS.USERID, TEMPBANS.MUTEDUNTIL).values(guild.getIdLong(), memberId, mutedUntil).execute();

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
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);
			ctx.deleteFrom(Tables.TEMPBANS).where(TEMPBANS.USERID.eq(memberId)).execute();
			ctx.deleteFrom(Tables.ROLES).where(ROLES.USER_ID.eq(memberId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
