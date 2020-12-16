package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.GuildChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Yaml;

public class YamlUtils
{
	private YamlUtils()
	{
		//Overrides the default, public constructor
	}
	public static boolean isFieldEmpty(String path, String filename)
	{
		return Yaml.getFieldString(path, filename) == null || Yaml.getFieldString(path, filename).isEmpty();
	}

	public static GuildChannel getLogChannel(String guildID)
	{
		return IGSQBot.getJDA().getGuildChannelById(Yaml.getFieldString(guildID + ".textlog", "guild"));
	}

	public static GuildChannel getVoidLogChannel(String guildID)
	{
		return IGSQBot.getJDA().getGuildChannelById(Yaml.getFieldString(guildID + ".voicelog", "guild"));
	}

	public static String fieldAppend(String path, String filename, String delimiter, Object data)
	{
		if(isFieldEmpty(path, filename))
		{
			return data + delimiter;
		}
		else
		{
			String onFile = Yaml.getFieldString(path, filename);

			onFile = onFile.strip();
			while(onFile.startsWith(delimiter))
			{
				onFile = onFile.substring(0,1);
			}

			if(onFile.endsWith(delimiter))
			{
				return onFile + data;
			}
			else
			{
				return onFile + delimiter + data;
			}
		}
	}

	public static String getGuildPrefix(String guildId)
	{
		return isFieldEmpty(guildId + ".prefix", "guild") ? Common.DEFAULT_BOT_PREFIX : Yaml.getFieldString(guildId + ".prefix", "guild").trim();
	}

	public static void setGuildPrefix(String guildId, String prefix)
	{
		Yaml.updateField(guildId + ".prefix", "guild", " " + prefix);
	}
}
