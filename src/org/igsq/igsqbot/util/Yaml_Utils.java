package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.GuildChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;

public class Yaml_Utils
{
	private Yaml_Utils()
	{
		//Overrides the default, public constructor
	}
	public static boolean isFieldEmpty(String path, String filename)
	{
		return Yaml.getFieldString(path, filename) == null || Yaml.getFieldString(path, filename).isEmpty();
	}

	public static GuildChannel getLogChannel(String guildID)
	{
		return Common.getJda().getGuildChannelById(Yaml.getFieldString(guildID + ".textlog", "guild"));
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
}
