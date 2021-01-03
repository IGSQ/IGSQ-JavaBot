package org.igsq.igsqbot.entities.cache;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Member;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.entities.json.*;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PunishmentCache implements IJsonCacheable
{
	private static final PunishmentCache INSTANCE = new PunishmentCache();
	private final Map<String, Punishment> cachedFiles = new ConcurrentHashMap<>();

	public static PunishmentCache getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void set(IJsonEntity json)
	{
		if(json instanceof Punishment)
		{
			cachedFiles.put(json.getPrimaryKey(), (Punishment) json);
		}
	}

	public Punishment get(String guildId, String userId)
	{
		if(cachedFiles.containsKey(guildId + userId))
		{
			return cachedFiles.get(guildId + userId);
		}
		else
		{
			Punishment newPunishment = new Punishment(guildId, userId);
			cachedFiles.put(guildId + userId, newPunishment);
			return newPunishment;
		}
	}

	public Punishment get(Member member)
	{
		String guildId = member.getGuild().getId();
		String userId = member.getId();
		if(cachedFiles.containsKey(guildId + userId))
		{
			return cachedFiles.get(guildId + userId);
		}
		else
		{
			Punishment newPunishment = new Punishment(guildId, member.getId());
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
	public void remove(IJsonEntity json)
	{
		cachedFiles.remove(json.getPrimaryKey());
	}

	@Override
	public void load()
	{
		try
		{
			Punishment[] punishments = Json.get(Punishment[].class, Filename.PUNISHMENT);
			if(punishments != null)
			{
				for(Punishment punishment : punishments)
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
		for(Punishment punishment : cachedFiles.values())
		{
			json.add(punishment.toJson());
		}
		Json.updateFile(json, Filename.PUNISHMENT);
	}

	@Override
	public void reload()
	{
		save();
		load();
	}

	public Report getReport(String messageId)
	{
		for(Punishment punishment : cachedFiles.values())
		{
			for(Report report : punishment.getReports())
			{
				if(report.getMessageId().equals(messageId))
				{
					return report;
				}
			}
		}
		return null;
	}

	public Collection<Punishment> getAll()
	{
		return cachedFiles.values();
	}
}
