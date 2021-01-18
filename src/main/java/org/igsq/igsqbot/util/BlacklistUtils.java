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

import static org.igsq.igsqbot.entities.jooq.tables.Blacklists.BLACKLISTS;

public class BlacklistUtils
{
	private BlacklistUtils()
	{
		//Overrides the default, public, constructor
	}

	public static List<String> LINKS = new ArrayList<>(List.of("youtube", "twitch", "ttv", "utube"));

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


		if(member == null || (findLink(content) && !event.getChannel().equals(promoChannel)))
		{
			return true;
		}

		return !member.hasPermission(Permission.MESSAGE_MANAGE) && !member.getRoles().contains(promoBypass);
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

	public static List<String> getBlacklistedPhrases(Guild guild, IGSQBot igsqBot)
	{
		List<String> result = new ArrayList<>();
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.selectFrom(Tables.BLACKLISTS)
					.where(BLACKLISTS.GUILD_ID.eq(guild.getIdLong()));

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
					.insertInto(Tables.BLACKLISTS)
					.columns(BLACKLISTS.GUILD_ID, BLACKLISTS.PHRASE)
					.values(guild.getIdLong(), phrase);

			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static boolean removePhrase(Guild guild, String phrase, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context
					.deleteFrom(Tables.BLACKLISTS)
					.where(BLACKLISTS.GUILD_ID.eq(guild.getIdLong()).and(BLACKLISTS.PHRASE.eq(phrase)));

			return query.execute() > 0;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}
}
