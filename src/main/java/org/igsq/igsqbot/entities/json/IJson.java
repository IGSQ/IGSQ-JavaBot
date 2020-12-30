package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;

public interface IJson
{
	JsonObject toJson();
	String getPrimaryKey();
	void remove();
}
