package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JsonPunishment implements IJson
{
	private String guildId;
	private String userId;
	private List<String> warnings = new ArrayList<>();
	private boolean isMuted;
	private long mutedUntil;
	private List<String> roles = new ArrayList<>();

	public JsonPunishment(String guildId, String userId)
	{
		this.guildId = guildId;
		this.userId = userId;
	}

	public String getGuildId()
	{
		return guildId;
	}

	public void setGuildId(String guildId)
	{
		this.guildId = guildId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public List<String> getWarnings()
	{
		return warnings;
	}

	public void setWarnings(List<String> warnings)
	{
		this.warnings = warnings;
	}

	public boolean isMuted()
	{
		return isMuted;
	}

	public void setMuted(boolean muted)
	{
		isMuted = muted;
	}

	public long getMutedUntil()
	{
		return mutedUntil;
	}

	public void setMutedUntil(long mutedUntil)
	{
		this.mutedUntil = mutedUntil;
	}

	public List<String> getRoles()
	{
		return roles;
	}

	public void setRoles(List<String> roles)
	{
		this.roles = roles;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		JsonArray warnings = new JsonArray();
		JsonArray roles = new JsonArray();
		this.warnings.forEach(warnings::add);
		this.roles.forEach(roles::add);

		jsonObject.addProperty("guildId", guildId);
		jsonObject.addProperty("userId", userId);
		jsonObject.add("warnings", warnings);
		jsonObject.addProperty("isMuted", isMuted);
		jsonObject.addProperty("mutedUntil", mutedUntil);
		jsonObject.add("roles", roles);
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return guildId + userId;
	}

	@Override
	public void remove()
	{
		JsonPunishmentCache.getInstance().remove(this);
	}
}
