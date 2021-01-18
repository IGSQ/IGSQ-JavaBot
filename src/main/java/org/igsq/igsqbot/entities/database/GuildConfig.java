package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.command.CommandContext;
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
		return getValue(GUILDS.SELF_PROMO_ROLE);
	}

	public long getLevelUpBot()
	{
		return getValue(GUILDS.LEVEL_UP_BOT);
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
		return getValue(GUILDS.REPORT_CHANNEL);
	}

	public long getLogChannel()
	{
		return getValue(GUILDS.LOG_CHANNEL);
	}

	public long getVoteChannel()
	{
		return getValue(GUILDS.VOTE_CHANNEL);
	}

	public long getTempBanRole()
	{
		return getValue(GUILDS.MUTED_ROLE);
	}

	public long getSuggestionChannel()
	{
		return getValue(GUILDS.SUGGESTION_CHANNEL);
	}

	public long getPromoChannel()
	{
		return getValue(GUILDS.SELF_PROMO_CHANNEL);
	}

	private long getValue(Field<?> value)
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
}
