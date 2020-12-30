package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.ArrayList;
import java.util.List;

public class JsonGuild implements IJson
{
	private final String guildId;
	private final ShardManager shardManager;
	private String verificationChannel = "";
	private List<JsonReactionRole> reactionRoles = new ArrayList<>();

	public JsonGuild(String guildId, ShardManager shardManager)
	{
		this.guildId = guildId;
		this.shardManager = shardManager;
	}

	public TextChannel getVerificationChannel()
	{
		return shardManager.getTextChannelById(verificationChannel);
	}

	public void setVerificationChannel(String newId)
	{
		verificationChannel = newId;
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
		JsonGuildCache.remove(this);
	}
	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		reactionRoles.forEach(reactionRole -> jsonArray.add(reactionRole.toJson()));

		jsonObject.addProperty("guildId", guildId);
		jsonObject.addProperty("verificationChannel", verificationChannel);
		jsonObject.add("reactionRoles", jsonArray);
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return guildId;
	}
}


