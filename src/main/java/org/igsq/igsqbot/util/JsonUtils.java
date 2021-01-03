package org.igsq.igsqbot.util;

import org.igsq.igsqbot.entities.json.Punishment;
import org.igsq.igsqbot.entities.cache.PunishmentCache;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils
{
	public static List<Punishment> getExpiredMutes()
	{
		List<Punishment> result = new ArrayList<>();
		for(Punishment selectedPunishment: PunishmentCache.getInstance().getAll())
		{
			if(selectedPunishment.isMuted() && System.currentTimeMillis() >= selectedPunishment.getMutedUntil())
			{
				result.add(selectedPunishment);
			}
		}
		return result;
	}
}
