package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonGuildCache
{
	private static final Map<String, JsonGuild> CACHE_MAP = new ConcurrentHashMap<>();

	private JsonGuildCache()
	{
		//Overrides the default, public, constructor
	}

	public static void load()
	{
		try
		{
			JsonGuild[] jsonGuilds = Json.get(JsonGuild[].class, Filename.GUILD);
			if(jsonGuilds != null)
			{
				for(JsonGuild guild : jsonGuilds)
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

	public static void set(JsonGuild guild)
	{
		CACHE_MAP.put(guild.getPrimaryKey(), guild);
	}

	public static JsonGuild get(String guildId, ShardManager shardManager)
	{
		if(CACHE_MAP.containsKey(guildId))
		{
			return CACHE_MAP.get(guildId);
		}
		else
		{
			return CACHE_MAP.computeIfAbsent(guildId, k -> new JsonGuild(guildId, shardManager));
		}
	}

	public static void remove(JsonGuild guild)
	{
		CACHE_MAP.remove(guild.getPrimaryKey());
	}

	public static void remove(String guildId)
	{
		CACHE_MAP.remove(guildId);
	}

	public static void save()
	{
		List<JsonObject> json = new ArrayList<>();
		for(JsonGuild guild : CACHE_MAP.values())
		{
			json.add(guild.toJson());
		}
		Json.updateFile(json, Filename.GUILD);
	}
}
