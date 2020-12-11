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
}
