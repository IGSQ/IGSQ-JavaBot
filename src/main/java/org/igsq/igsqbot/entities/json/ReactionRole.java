package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;
import org.igsq.igsqbot.entities.cache.GuildConfigCache;

import java.util.List;

public class ReactionRole implements IJsonEntity
{
	private final String guildId;
	private final String channelId;
	private final String messageId;
	private final String emote;
	private final String role;

	public ReactionRole(String guildId, String channelId, String messageId, String emote, String role)
	{
		this.guildId = guildId;
		this.channelId = channelId;
		this.messageId = messageId;
		this.emote = emote;
		this.role = role;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("guildId", guildId);
		jsonObject.addProperty("channelId", channelId);
		jsonObject.addProperty("messageId", messageId);
		jsonObject.addProperty("emote", emote);
		jsonObject.addProperty("role", role);
		return jsonObject;
	}

	public String getGuildId()
	{
		return guildId;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public String getMessageId()
	{
		return messageId;
	}

	public String getEmote()
	{
		return emote;
	}

	public String getRole()
	{
		return role;
	}

	@Override
	public String getPrimaryKey()
	{
		return emote;
	}

	@Override
	public void remove()
	{
		GuildConfig config = GuildConfigCache.getInstance().get(guildId);
		List<ReactionRole> reactionRoles = config.getReactionRoles();
		reactionRoles.removeIf(reactionRole -> reactionRole.getPrimaryKey().equals(getPrimaryKey()));
		config.setReactionRoles(reactionRoles);
	}
}
