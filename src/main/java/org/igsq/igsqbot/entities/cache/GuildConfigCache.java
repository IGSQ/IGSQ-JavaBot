package org.igsq.igsqbot.entities.cache;

import com.google.gson.JsonObject;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.GuildConfig;
import org.igsq.igsqbot.entities.json.IJsonCacheable;
import org.igsq.igsqbot.entities.json.IJsonEntity;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuildConfigCache implements IJsonCacheable
{
	private static final GuildConfigCache INSTANCE = new GuildConfigCache();
	private final Map<String, GuildConfig> cachedFiles = new ConcurrentHashMap<>();

	private GuildConfigCache()
	{
		//Overrides the default, public, constructor
	}

	public static GuildConfigCache getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void load()
	{
		try
		{
			GuildConfig[] guildConfigs = Json.get(GuildConfig[].class, Filename.GUILD);
			if(guildConfigs != null)
			{
				for(GuildConfig guild : guildConfigs)
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
	public void set(IJsonEntity guild)
	{
		if(guild instanceof GuildConfig)
		{
			cachedFiles.put(guild.getPrimaryKey(), (GuildConfig) guild);
		}
	}

	public GuildConfig get(String guildId)
	{
		if(cachedFiles.containsKey(guildId))
		{
			return cachedFiles.get(guildId);
		}
		else
		{
			GuildConfig newGuild = new GuildConfig(guildId);
			cachedFiles.put(guildId, newGuild);
			return newGuild;
		}
	}

	public void remove(String guildId)
	{
		cachedFiles.remove(guildId);
	}

	@Override
	public void remove(IJsonEntity json)
	{
		cachedFiles.remove(json.getPrimaryKey());
	}

	@Override
	public void save()
	{
		List<JsonObject> json = new ArrayList<>();
		for(GuildConfig guild : cachedFiles.values())
		{
			json.add(guild.toJson());
		}
		Json.updateFile(json, Filename.GUILD);
	}

	@Override
	public void reload()
	{
		save();
		load();
	}
}
