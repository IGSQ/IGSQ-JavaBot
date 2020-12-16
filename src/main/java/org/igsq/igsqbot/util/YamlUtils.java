package main.java.org.igsq.igsqbot.util;

import main.java.org.igsq.igsqbot.Common;
import main.java.org.igsq.igsqbot.IGSQBot;
import main.java.org.igsq.igsqbot.Yaml;
import net.dv8tion.jda.api.entities.GuildChannel;

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
