package org.igsq.igsqbot.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.entities.jooq.tables.pojos.ChannelBlacklists;

import static org.igsq.igsqbot.entities.jooq.tables.ChannelBlacklists.CHANNEL_BLACKLISTS;
import static org.igsq.igsqbot.entities.jooq.tables.WordBlacklists.WORD_BLACKLISTS;

public class BlacklistUtils
{
	private BlacklistUtils()
	{
		//Overrides the default, public, constructor
	}

	public static final List<String> LINKS = List.copyOf(List.of("youtube.com/", "twitch.tv/", "youtu.be/"));
	public static final List<String> DISCORD = List.copyOf(List.of("discord.gg/"));

	public static boolean isBlacklistedPhrase(MessageReceivedEvent event, IGSQBot igsqBot)
	{
		Guild guild = event.getGuild();
		String content = event.getMessage().getContentRaw();

		List<String> blacklistedWords = getBlacklistedPhrases(guild, igsqBot);

		for(String word : content.split("\\s+"))
		{
			if(blacklistedWords.contains(word))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isChannelBlacklisted(MessageReceivedEvent event, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.CHANNEL_BLACKLISTS)
					.where(CHANNEL_BLACKLISTS.CHANNEL_ID.eq(event.getChannel().getIdLong())
					.and(CHANNEL_BLACKLISTS.GUILD_ID.eq(event.getGuild().getIdLong())));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static boolean isAdvertising(MessageReceivedEvent event, IGSQBot igsqBot)
	{
		Guild guild = event.getGuild();
		String content = event.getMessage().getContentRaw();
		Member member = event.getMember();

		MessageChannel promoChannel = guild.getTextChannelById(new GuildConfig(guild.getIdLong(), igsqBot).getPromoChannel());
		Role promoBypass = guild.getRoleById(new GuildConfig(guild.getIdLong(), igsqBot).getPromoRole());

		if(promoChannel == null || promoBypass == null)
		{
			return false;
		}


		if(findLink(content))
		{
			if(member == null)
			{
				return true;
			}

			if(member.hasPermission(Permission.MESSAGE_MANAGE))
			{
				return false;
			}

			if(!event.getChannel().equals(promoChannel))
			{
				return false;
			}

			return event.getChannel().equals(promoChannel) && !member.getRoles().contains(promoBypass);
		}
		return false;
	}

	private static boolean findLink(String content)
	{
		content = content.replaceAll("\\s+", "");
		content = content.toLowerCase();

		for(String link : LINKS)
		{
			if(content.contains(link))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isDiscordInvite(MessageReceivedEvent event)
	{
		Member member = event.getMember();

		if(member == null)
		{
			return false;
		}

		if(member.hasPermission(Permission.MESSAGE_MANAGE))
		{
			return false;
		}
		String content = event.getMessage().getContentRaw();
		content = content.replaceAll("\\s+", "");
		content = content.toLowerCase();

		for(String link : DISCORD)
		{
			if(content.contains(link))
			{
				return true;
			}
		}
		return false;
	}

	public static List<String> getBlacklistedPhrases(Guild guild, IGSQBot igsqBot)
	{
		List<String> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.WORD_BLACKLISTS)
					.where(WORD_BLACKLISTS.GUILD_ID.eq(guild.getIdLong()));

			for(var row : query.fetch())
			{
				result.add(row.getPhrase());
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}

	public static void addPhrase(Guild guild, String phrase, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(Tables.WORD_BLACKLISTS)
					.columns(WORD_BLACKLISTS.GUILD_ID, WORD_BLACKLISTS.PHRASE)
					.values(guild.getIdLong(), phrase);

			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static boolean addChannel(MessageChannel channel, Guild guild, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.insertInto(Tables.CHANNEL_BLACKLISTS)
					.columns(CHANNEL_BLACKLISTS.GUILD_ID, CHANNEL_BLACKLISTS.CHANNEL_ID)
					.values(guild.getIdLong(), channel.getIdLong());

			var exists = context
				.selectFrom(Tables.CHANNEL_BLACKLISTS)
				.where(CHANNEL_BLACKLISTS.GUILD_ID.eq(guild.getIdLong())
				.and(CHANNEL_BLACKLISTS.CHANNEL_ID.eq(channel.getIdLong())));

			if(exists.fetch().isNotEmpty())
			{
				return false;
			}

			query.execute();
			return true;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static List<ChannelBlacklists> getBlacklistedChannels(Guild guild, IGSQBot igsqBot)
	{
		List<ChannelBlacklists> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.CHANNEL_BLACKLISTS)
					.where(CHANNEL_BLACKLISTS.GUILD_ID.eq(guild.getIdLong()));

			for(var row : query.fetch())
			{
				result.add(new ChannelBlacklists(row.getId(), row.getGuildId(), row.getChannelId()));
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
		return result;
	}

	public static boolean removeChannel(MessageChannel channel, Guild guild, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(Tables.CHANNEL_BLACKLISTS)
					.where(CHANNEL_BLACKLISTS.GUILD_ID.eq(guild.getIdLong())
					.and(CHANNEL_BLACKLISTS.CHANNEL_ID.eq(channel.getIdLong())));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static boolean removePhrase(Guild guild, String phrase, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(Tables.WORD_BLACKLISTS)
					.where(WORD_BLACKLISTS.GUILD_ID.eq(guild.getIdLong()).and(WORD_BLACKLISTS.PHRASE.eq(phrase)));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}
}
