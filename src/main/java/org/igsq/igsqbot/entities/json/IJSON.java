package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;

public interface IJSON
{
	JsonObject toJson();
	String getPrimaryKey();
}
