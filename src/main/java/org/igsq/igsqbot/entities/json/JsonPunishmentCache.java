package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Member;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonPunishmentCache implements IJsonCacheable
{
	private static final JsonPunishmentCache INSTANCE = new JsonPunishmentCache();
	private final Map<String, JsonPunishment> cachedFiles = new ConcurrentHashMap<>();

	public static JsonPunishmentCache getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void set(IJson json)
	{
		System.out.println("JSON ADDED: " + json.getPrimaryKey());
		if(json instanceof JsonPunishment)
		{
			cachedFiles.put(json.getPrimaryKey(), (JsonPunishment) json);
		}
	}

	public IJson get(String guildId, String userId)
	{
		if(cachedFiles.containsKey(guildId + userId))
		{
			return cachedFiles.get(guildId + userId);
		}
		else
		{
			JsonPunishment newPunishment = new JsonPunishment(guildId, userId);
			cachedFiles.put(guildId + userId, newPunishment);
			return newPunishment;
		}
	}

	public JsonPunishment get(Member member)
	{
		String guildId = member.getGuild().getId();
		String userId = member.getId();
		if(cachedFiles.containsKey(guildId + userId))
		{
			return cachedFiles.get(guildId + userId);
		}
		else
		{
			JsonPunishment newPunishment = new JsonPunishment(guildId, member.getId());
			cachedFiles.put(guildId + userId, newPunishment);
			return newPunishment;
		}
	}

	@Override
	public void remove(String primaryKey)
	{
		cachedFiles.remove(primaryKey);
	}

	@Override
	public void remove(IJson json)
	{
		cachedFiles.remove(json.getPrimaryKey());
	}

	@Override
	public void load()
	{
		try
		{
			JsonPunishment[] punishments = Json.get(JsonPunishment[].class, Filename.PUNISHMENT);
			if(punishments != null)
			{
				for(JsonPunishment punishment : punishments)
				{
					cachedFiles.put(punishment.getPrimaryKey(), punishment);
				}
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			load();
		}
	}

	@Override
	public void save()
	{
		List<JsonObject> json = new ArrayList<>();
		for(JsonPunishment punishment : cachedFiles.values())
		{
			json.add(punishment.toJson());
		}
		Json.updateFile(json, Filename.PUNISHMENT);
	}

	public Collection<JsonPunishment> getAll()
	{
		return cachedFiles.values();
	}
}
