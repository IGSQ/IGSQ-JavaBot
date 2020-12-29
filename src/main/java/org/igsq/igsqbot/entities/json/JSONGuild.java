package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

public class JSONGuild implements IJSON
{
	private final String guildId;
	private final JDA jda;
	private String verificationChannel = "";

	public JSONGuild(String guildId, JDA jda)
	{
		this.guildId = guildId;
		this.jda = jda;
	}

	public TextChannel getVerificationChannel()
	{
		return jda.getTextChannelById(verificationChannel);
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("guildId", guildId);
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return guildId;
	}
}


