package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.jooq.Field;

import static org.igsq.igsqbot.entities.jooq.tables.Guilds.GUILDS;

public class GuildConfig
{
	private final IGSQBot igsqBot;
	private final long guildId;

	public GuildConfig(CommandContext ctx)
	{
		this.guildId = ctx.getGuild().getIdLong();
		this.igsqBot = ctx.getIGSQBot();
	}

	public GuildConfig(long guildId, IGSQBot igsqBot)
	{
		this.guildId = guildId;
		this.igsqBot = igsqBot;
	}

	public GuildConfig(Guild guild, IGSQBot igsqBot)
	{
		this.guildId = guild.getIdLong();
		this.igsqBot = igsqBot;
	}

	public String getPrefix()
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.select(GUILDS.PREFIX).from(GUILDS).where(GUILDS.GUILD_ID.eq(guildId));
			String result = query.fetchOne(GUILDS.PREFIX);
			query.close();
			return result;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return ".";
		}
	}

	public long getPromoRole()
	{
		return getLong(GUILDS.SELF_PROMO_ROLE);
	}

	public long getLevelUpBot()
	{
		return getLong(GUILDS.LEVEL_UP_BOT);
	}

	public void setPrefix(String prefix)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.update(GUILDS).set(GUILDS.PREFIX, prefix).where(GUILDS.GUILD_ID.eq(guildId));
			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public long getReportChannel()
	{
		return getLong(GUILDS.REPORT_CHANNEL);
	}

	public long getLogChannel()
	{
		return getLong(GUILDS.LOG_CHANNEL);
	}

	public long getVoteChannel()
	{
		return getLong(GUILDS.VOTE_CHANNEL);
	}

	public long getTempBanRole()
	{
		return getLong(GUILDS.MUTED_ROLE);
	}

	public long getSuggestionChannel()
	{
		return getLong(GUILDS.SUGGESTION_CHANNEL);
	}

	public long getPromoChannel()
	{
		return getLong(GUILDS.SELF_PROMO_CHANNEL);
	}

	public long getChannelSuggestionChannel()
	{
		return getLong(GUILDS.CHANNEL_SUGGESTION_CHANNEL);
	}

	public void setLevelUpBot(long userId)
	{
		setValue(GUILDS.LEVEL_UP_BOT, userId);
	}

	private long getLong(Field<?> value)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.select(value).from(GUILDS).where(GUILDS.GUILD_ID.eq(guildId));
			long result = Long.parseLong(String.valueOf(query.fetchOne(value)));
			query.close();
			return result;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}

	private <T> void setValue(Field<T> field, T value)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			context.update(Tables.GUILDS).set(field, value).where(GUILDS.GUILD_ID.eq(guildId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
