package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.igsq.igsqbot.entities.cache.GuildConfigCache;

import java.util.ArrayList;
import java.util.List;

public class GuildConfig implements IJsonEntity
{
	private String guildId;
	private List<ReactionRole> reactionRoles = new ArrayList<>();
	private String mutedRole;
	private String reportChannel;
	private String verifiedRole;

	public String getReportChannel()
	{
		return reportChannel;
	}

	public void setReportChannel(String reportChannel)
	{
		this.reportChannel = reportChannel;
	}

	public String getVerifiedRole()
	{
		return verifiedRole;
	}

	public void setVerifiedRole(String verifiedRole)
	{
		this.verifiedRole = verifiedRole;
	}

	public GuildConfig(String guildId)
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

	public List<ReactionRole> getReactionRoles()
	{
		return reactionRoles;
	}

	public void setReactionRoles(List<ReactionRole> newReactionRoles)
	{
		reactionRoles = newReactionRoles;
	}

	@Override
	public void remove()
	{
		GuildConfigCache.getInstance().remove(this);
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
		jsonObject.addProperty("reportChannel", reportChannel);
		jsonObject.addProperty("verifiedRole", verifiedRole);
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return guildId;
	}
}


