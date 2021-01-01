package org.igsq.igsqbot.util;

import org.igsq.igsqbot.entities.json.JsonPunishment;
import org.igsq.igsqbot.entities.json.JsonPunishmentCache;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils
{
	public static List<JsonPunishment> getExpiredMutes()
	{
		List<JsonPunishment> result = new ArrayList<>();
		for(JsonPunishment selectedPunishment: JsonPunishmentCache.getInstance().getAll())
		{
			if(selectedPunishment.isMuted() && System.currentTimeMillis() >= selectedPunishment.getMutedUntil())
			{
				result.add(selectedPunishment);
			}
		}
		return result;
	}
}
