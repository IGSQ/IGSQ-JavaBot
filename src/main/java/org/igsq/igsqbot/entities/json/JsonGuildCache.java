package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonGuildCache implements IJsonCacheable
{
	private static final JsonGuildCache INSTANCE = new JsonGuildCache();
	private final Map<String, JsonGuild> cachedFiles = new ConcurrentHashMap<>();

	private JsonGuildCache()
	{
		//Overrides the default, public, constructor
	}

	@Override
	public void load()
	{
		try
		{
			JsonGuild[] jsonGuilds = Json.get(JsonGuild[].class, Filename.GUILD);
			if(jsonGuilds != null)
			{
				for(JsonGuild guild : jsonGuilds)
				{
					cachedFiles.put(guild.getPrimaryKey(), guild);
				}
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			System.exit(1);
		}
	}

	@Override
	public void set(IJson guild)
	{
		if(guild instanceof JsonGuild)
		{
			cachedFiles.put(guild.getPrimaryKey(), (JsonGuild) guild);
		}
	}

	public JsonGuild get(String guildId, ShardManager shardManager)
	{
		if(cachedFiles.containsKey(guildId))
		{
			return cachedFiles.get(guildId);
		}
		else
		{
			return cachedFiles.computeIfAbsent(guildId, k -> new JsonGuild(guildId, shardManager));
		}
	}

	public void remove(String guildId)
	{
		cachedFiles.remove(guildId);
	}

	@Override
	public void remove(IJson json)
	{
		cachedFiles.remove(json.getPrimaryKey());
	}

	@Override
	public void save()
	{
		List<JsonObject> json = new ArrayList<>();
		for(JsonGuild guild : cachedFiles.values())
		{
			json.add(guild.toJson());
		}
		Json.updateFile(json, Filename.GUILD);
	}

	public static JsonGuildCache getInstance()
	{
		return INSTANCE;
	}
}
