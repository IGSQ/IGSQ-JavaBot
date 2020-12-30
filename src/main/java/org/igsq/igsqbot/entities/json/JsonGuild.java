package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class JsonGuild implements IJson
{
	private String guildId;
	private Map<String, JsonReactionRole[]> reactionRoles = new HashMap<>();
	private String mutedRole;

	public JsonGuild(String guildId)
	{
		this.guildId = guildId;
	}

	public void setGuildId(String guildId)
	{
		this.guildId = guildId;
	}

	public String getMutedRole()
	{
		return mutedRole;
	}

	public void setMutedRole(String mutedRole)
	{
		this.mutedRole = mutedRole;
	}

	public Map<String, JsonReactionRole> getReactionRoles()
	{
		return reactionRoles;
	}

	public void setReactionRoles(Map<String, JsonReactionRole> newReactionRoles)
	{
		reactionRoles = newReactionRoles;
	}

	@Override
	public void remove()
	{
		JsonGuildCache.getInstance().remove(this);
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();

		reactionRoles.values().forEach(reactionRole -> jsonArray.add(reactionRole.toJson()));

		jsonObject.addProperty("guildId", guildId);
		jsonObject.add("reactionRoles", jsonArray);
		jsonObject.addProperty("mutedRole", mutedRole);
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return guildId;
	}
}


