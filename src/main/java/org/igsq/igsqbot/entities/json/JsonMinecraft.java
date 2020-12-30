package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMinecraft implements IJson
{
	private final List<String> squirrel = new ArrayList<>();
	private final List<String> rising = new ArrayList<>();
	private final List<String> flying = new ArrayList<>();
	private final List<String> soaring = new ArrayList<>();
	private final List<String> epic = new ArrayList<>();
	private final List<String> epic2 = new ArrayList<>();
	private final List<String> epic3 = new ArrayList<>();
	private final List<String> elite = new ArrayList<>();
	private final List<String> elite2 = new ArrayList<>();
	private final List<String> elite3 = new ArrayList<>();
	private final List<String> mod = new ArrayList<>();
	private final List<String> mod2 = new ArrayList<>();
	private final List<String> mod3 = new ArrayList<>();
	private final List<String> council = new ArrayList<>();
	private final List<String> birthday = new ArrayList<>();
	private final List<String> nitroboost = new ArrayList<>();
	private final List<String> retired = new ArrayList<>();
	private final List<String> founder = new ArrayList<>();
	private final List<String> developer = new ArrayList<>();
	private final List<String> supporter = new ArrayList<>();

	public Map<List<String>, String> getRanks()
	{
		Map<List<String>, String> result = new HashMap<>();
		result.put(squirrel, "default");
		result.put(rising, "rising");
		result.put(flying, "flying");
		result.put(soaring, "soaring");
		result.put(epic, "epic");
		result.put(epic2, "epic2");
		result.put(epic3, "epic3");
		result.put(elite, "elite");
		result.put(elite2, "elite2");
		result.put(elite3, "elite3");
		result.put(mod, "mod");
		result.put(mod2, "mod2");
		result.put(mod3, "mod3");
		result.put(council, "council");
		result.put(birthday, "birthday");
		result.put(nitroboost, "nitroboost");
		result.put(retired, "retired");
		result.put(founder, "founder");
		result.put(developer, "developer");
		result.put(supporter, "supporter");
		return result;
	}

	public List<String> getNitroboost()
	{
		return nitroboost;
	}

	public List<String> getDeveloper()
	{
		return developer;
	}

	public List<String> getBirthday()
	{
		return birthday;
	}

	public List<String> getFounder()
	{
		return founder;
	}

	public List<String> getRetired()
	{
		return retired;
	}

	public List<String> getSupporter()
	{
		return supporter;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();
		JsonArray exampleArray = new JsonArray();
		exampleArray.add(new JsonPrimitive("010101010101010101"));

		jsonObject.add("squirrel", exampleArray);
		jsonObject.add("rising", exampleArray);
		jsonObject.add("flying", exampleArray);
		jsonObject.add("soaring", exampleArray);
		jsonObject.add("epic", exampleArray);
		jsonObject.add("epic2", exampleArray);
		jsonObject.add("epic3", exampleArray);
		jsonObject.add("elite", exampleArray);
		jsonObject.add("elite2", exampleArray);
		jsonObject.add("elite3", exampleArray);
		jsonObject.add("mod", exampleArray);
		jsonObject.add("mod2", exampleArray);
		jsonObject.add("mod3", exampleArray);
		jsonObject.add("council", exampleArray);
		jsonObject.add("birthday", exampleArray);
		jsonObject.add("nitroboost", exampleArray);
		jsonObject.add("retired", exampleArray);
		jsonObject.add("founder", exampleArray);
		jsonObject.add("developer", exampleArray);
		jsonObject.add("supporter", exampleArray);
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
