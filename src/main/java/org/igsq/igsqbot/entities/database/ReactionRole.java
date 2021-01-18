package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;

import static org.igsq.igsqbot.entities.jooq.tables.ReactionRoles.REACTION_ROLES;

public class ReactionRole
{
	private final long messageId;
	private final long roleId;
	private final String emote;
	private final IGSQBot igsqBot;
	private final long guildId;

	public ReactionRole(long messageId, long roleId, long guildId, String emote, IGSQBot igsqBot)
	{
		this.messageId = messageId;
		this.roleId = roleId;
		this.guildId = guildId;
		this.emote = emote;
		this.igsqBot = igsqBot;
	}

	public static List<ReactionRole> getByMessageId(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(REACTION_ROLES)
					.where(REACTION_ROLES.MESSAGE_ID.eq(messageId));

			var result = query.fetch();
			query.close();

			if(result.isEmpty())
			{
				return Collections.emptyList();
			}
			else
			{
				List<ReactionRole> reactionRoles = new ArrayList<>();
				for(var rr : result)
				{
					reactionRoles.add(new ReactionRole(rr.getMessageId(), rr.getRoleId(), rr.getGuildId(), rr.getEmoteId(), igsqBot));
				}
				return reactionRoles;
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return Collections.emptyList();
		}
	}

	public void add()
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(REACTION_ROLES)
					.columns(REACTION_ROLES.GUILD_ID, REACTION_ROLES.MESSAGE_ID, REACTION_ROLES.ROLE_ID, REACTION_ROLES.EMOTE_ID)
					.values(guildId, messageId, roleId, emote);
			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public long getMessageId()
	{
		return messageId;
	}

	public long getRoleId()
	{
		return roleId;
	}

	public String getEmote()
	{
		return emote;
	}

	public long getGuildId()
	{
		return guildId;
	}

	public void addRole(Member member)
	{
		Guild guild = igsqBot.getShardManager().getGuildById(guildId);
		if(guild != null)
		{
			Role role = guild.getRoleById(roleId);
			if(role != null)
			{
				guild.addRoleToMember(member, role).queue();
			}
		}
	}

	public void removeRole(Member member)
	{
		Guild guild = igsqBot.getShardManager().getGuildById(guildId);
		if(guild != null)
		{
			Role role = guild.getRoleById(roleId);
			if(role != null)
			{
				guild.removeRoleFromMember(member, role).queue();
			}
		}
	}

	public void remove()
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(REACTION_ROLES)
					.where(REACTION_ROLES.MESSAGE_ID.eq(messageId).and(REACTION_ROLES.ROLE_ID.eq(roleId)).and(REACTION_ROLES.EMOTE_ID.eq(emote)));
			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public boolean isPresent()
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(REACTION_ROLES)
					.where(REACTION_ROLES.MESSAGE_ID.eq(messageId).and(REACTION_ROLES.EMOTE_ID.eq(emote)).and(REACTION_ROLES.ROLE_ID.eq(roleId)));

			var result = query.fetch();
			query.close();
			return !result.isEmpty();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}
}
