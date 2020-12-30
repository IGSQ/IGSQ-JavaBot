package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.ArrayList;
import java.util.List;

public class JsonGuild implements IJson
{
	private String guildId = null;
	private List<JsonReactionRole> reactionRoles = new ArrayList<>();

	private final ShardManager shardManager;
	public JsonGuild(String guildId, ShardManager shardManager)
	{
		this.guildId = guildId;
		this.shardManager = shardManager;
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
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return guildId;
	}
}


