package org.igsq.igsqbot.entities.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.igsq.igsqbot.Json;

import java.util.ArrayList;
import java.util.List;

public class JsonReactionRole implements IJson
{
	private final String guildId;
	private final String channelId;
	private final String messageId;
	private final String emote;
	private final String role;

	public JsonReactionRole(String guildId, String channelId, String messageId, String emote, String role)
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
		jsonObject.addProperty("emote", String.valueOf(emote));
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
		JsonArray onFile = Json.get(JsonArray.class, Filename.GUILD);
		List<JsonReactionRole> reactionRoles = new ArrayList<>();
		if(onFile != null)
		{
			for(JsonElement element : onFile)
			{
				JsonGuild guild = new Gson().fromJson(element, JsonGuild.class);
				if(guild.getPrimaryKey().equals(this.guildId))
				{
					for(JsonReactionRole entry : guild.getReactionRoles())
					{
						if(!entry.getPrimaryKey().equals(this.getPrimaryKey()))
						{
							reactionRoles.add(entry);
						}
					}
					guild.setReactionRoles(reactionRoles);
					JsonGuildCache.getInstance().set(guild);
					break;
				}
			}
		}
	}
}
