package org.igsq.igsqbot.entities.cache;

import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.yaml.Filename;

import java.util.Map;
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
}
