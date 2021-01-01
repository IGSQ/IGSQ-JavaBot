package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JsonGuild implements IJson
{
	private String guildId;
	private List<JsonReactionRole> reactionRoles = new ArrayList<>();
	private String mutedRole;
	private String verifiedRole;

	public String getVerifiedRole()
	{
		return verifiedRole;
	}

	public void setVerifiedRole(String verifiedRole)
	{
		this.verifiedRole = verifiedRole;
	}

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

	public List<JsonReactionRole> getReactionRoles()
	{
		return reactionRoles;
	}

	public void setReactionRoles(List<JsonReactionRole> newReactionRoles)
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

		reactionRoles.forEach(reactionRole -> jsonArray.add(reactionRole.toJson()));

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


