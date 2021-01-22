package org.igsq.igsqbot.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.igsq.igsqbot.entities.Emoji;

public class StringUtils
{
	private StringUtils()
	{
		//Overrides the default, public, constructor
	}

	public static String getEmoteAsMention(String emote)
	{
		try
		{
			return "<:emote:" + Long.parseLong(emote) + ">";
		}
		catch(Exception exception)
		{
			return emote;
		}
	}

	public static String parseToEmote(int number)
	{
		return switch(number)
				{
					case 1 -> Emoji.ONE.getAsMessageable();
					case 2 -> Emoji.TWO.getAsMessageable();
					case 3 -> Emoji.THREE.getAsMessageable();
					case 4 -> Emoji.FOUR.getAsMessageable();
					case 5 -> Emoji.FIVE.getAsMessageable();
					case 6 -> Emoji.SIX.getAsMessageable();
					case 7 -> Emoji.SEVEN.getAsMessageable();
					case 8 -> Emoji.EIGHT.getAsMessageable();
					case 9 -> Emoji.NINE.getAsMessageable();
					case 0 -> Emoji.ZERO.getAsMessageable();
					default -> "";
				};
	}

	public static String getRoleAsMention(long roleId)
	{
		return "<@&" + roleId + ">";
	}

	public static String getChannelAsMention(String channelID)
	{
		return "<#" + channelID + ">";
	}

	public static String getChannelAsMention(long channelID)
	{
		return "<#" + channelID + ">";
	}

	public static String getTimestamp()
	{
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now());
	}

	public static String getMessageLink(long messageId, long channelId, long guildId)
	{
		return "https://discord.com/channels/" + guildId + "/" + channelId + "/" + messageId;
	}

	public static String parseDateTime(LocalDateTime time)
	{
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(time);
	}

	public static String parseDateTime(OffsetDateTime time)
	{
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(time);
	}

	public static String getUserAsMention(String userId)
	{
		return "<@!" + userId + ">";
	}

	public static String getUserAsMention(long userId)
	{
		return "<@!" + userId + ">";
	}
}
