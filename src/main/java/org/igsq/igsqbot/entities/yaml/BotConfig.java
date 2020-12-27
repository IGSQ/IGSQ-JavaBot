package org.igsq.igsqbot.entities.yaml;

import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BotConfig
{
	public String getToken()
	{
		return Yaml.getFieldString("bot.token", Filename.CONFIG);
	}

	public String getServer()
	{
		return Yaml.getFieldString("bot.server", Filename.CONFIG);
	}

	public String getErrorChannel()
	{
		return Yaml.getFieldString("bot.error", Filename.CONFIG);
	}

	public Map<String, String> getSQL()
	{
		Map<String, String> result = new ConcurrentHashMap<>();
		result.put("username", Yaml.getFieldString("mysql.username", Filename.CONFIG));
		result.put("password", Yaml.getFieldString("mysql.password", Filename.CONFIG));
		result.put("database", Yaml.getFieldString("mysql.database", Filename.CONFIG));
		return result;
	}

	public List<String> getPrivilegedUsers()
	{
		if(YamlUtils.isFieldEmpty("bot.privileged", Filename.CONFIG))
		{
			return Collections.emptyList();
		}
		else
		{
			return new ArrayList<>(Arrays.asList(Yaml.getFieldString("bot.privileged", Filename.CONFIG).split("/")));
		}
	}
}
