package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import org.igsq.igsqbot.JSON;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSONGuildCache
{
	private static final Map<String, JSONGuild> CACHE_MAP = new ConcurrentHashMap<>();
	static
	{
		try
		{
			JSONGuild[] jsonGuilds = (JSONGuild[]) JSON.get(JSONGuild[].class, Filename.GUILD);
			if(jsonGuilds != null)
			{
				for(JSONGuild guild : jsonGuilds)
				{
					CACHE_MAP.put(guild.getPrimaryKey(), guild);
				}
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			System.exit(1);
		}

	}
	private JSONGuildCache()
	{
		//Overrides the default, public, constructor
	}

	public static void set(JSONGuild guild)
	{
		CACHE_MAP.putIfAbsent(guild.getPrimaryKey(), guild);
	}

	public static JSONGuild get(String guildId, JDA jda)
	{
		if(CACHE_MAP.containsKey(guildId))
		{
			return CACHE_MAP.get(guildId);
		}
		else
		{
			return CACHE_MAP.computeIfAbsent(guildId, k -> new JSONGuild(guildId, jda));
		}
	}

	public static void save()
	{
		List<JsonObject> json = new ArrayList<>();
		for(JSONGuild guild : CACHE_MAP.values())
		{
			json.add(guild.toJson());
		}
		JSON.updateFile(json, Filename.GUILD);
	}
}
