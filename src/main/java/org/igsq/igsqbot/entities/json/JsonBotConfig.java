package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBotConfig implements IJson
{
	private final String botToken = "token";
	private final String botServer = "server";
	private final List<String> privilegedUsers = new ArrayList<>();
	private final String dbUsername = "username";
	private final String dbPassword = "password";
	private final String dbURL = "jdbc:mysql://localhost:3306/database";

	public String getToken()
	{
		return botToken;
	}

	public String getServer()
	{
		return botServer;
	}

	public List<String> getPrivilegedUsers()
	{
		return privilegedUsers;
	}

	public Map<String, String> getSQL()
	{
		Map<String, String> result = new HashMap<>();
		result.put("username", dbUsername);
		result.put("password", dbPassword);
		result.put("url", dbURL);
		return result;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		jsonArray.add(new JsonPrimitive("000000000000000000"));
		jsonArray.add(new JsonPrimitive("000000000000000001"));
		jsonArray.add(new JsonPrimitive("000000000000000002"));

		jsonObject.addProperty("botToken", botToken);
		jsonObject.addProperty("botServer", botServer);
		jsonObject.add("privilegedUsers", jsonArray);

		jsonObject.addProperty("dbUsername", dbUsername);
		jsonObject.addProperty("dbPassword", dbPassword);
		jsonObject.addProperty("dbURL", dbURL);


		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		//This does not have a primary key
		return "";
	}

	@Override
	public void remove()
	{
		//This will never require removal
	}
}
