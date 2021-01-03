package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;

public interface IJsonEntity
{
	JsonObject toJson();

	String getPrimaryKey();

	void remove();
}
