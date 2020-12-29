package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public class JSONBotConfig implements IJSON
{
	private String token;
	private String server;
	private String error;
	private List<String> privileged = new ArrayList<>();

	public String getToken()
	{
		return token;
	}

	public String getServer()
	{
		return server;
	}

	public String getError()
	{
		return error;
	}

	public List<String> getPrivileged()
	{
		return privileged;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		jsonArray.add(new JsonPrimitive("user"));
		jsonArray.add(new JsonPrimitive("user1"));

		jsonObject.addProperty("token", "token");
		jsonObject.addProperty("server", "server");
		jsonObject.addProperty("error", "error");
		jsonObject.add("privileged", jsonArray);
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return "";
	}
}
