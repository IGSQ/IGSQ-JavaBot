package org.igsq.igsqbot.handlers;

import org.igsq.igsqbot.objects.Command;

import java.util.HashMap;
import java.util.Map;

public abstract class CooldownHandler
{
	private static final Map<Command, Map<String, Long>> COOLDOWN_MAP = new HashMap<>(); // K = Command, V = Map<userId, Timestamp>

	private CooldownHandler()
	{
		//Overrides the default, public, constructor
	}

	public static void addCooldown(String userID, Command command)
	{
		final int cooldown = command.getCooldown();
		if(cooldown == 0)
		{
			return;
		}
		COOLDOWN_MAP.computeIfAbsent(command, k -> new HashMap<>()).put(userID, System.currentTimeMillis() + (command.getCooldown() * 1000L));
	}

	public static boolean isOnCooldown(String userID, Command command)
	{
		final Map<String, Long> listEntry = COOLDOWN_MAP.get(command);
		if(listEntry == null)
		{
			return false;
		}
		final long lastUsed = listEntry.get(userID);
		return lastUsed != 0 && System.currentTimeMillis() <= lastUsed;
	}

	public static long getCooldown(final String userID, final Command command)
	{
		return Math.abs((System.currentTimeMillis() - COOLDOWN_MAP.get(command).get(userID)) / 1000L);
	}
}
