package org.igsq.igsqbot.handlers;

import java.util.HashMap;
import java.util.Map;
import org.igsq.igsqbot.entities.Command;

public abstract class CooldownHandler
{
    private static final Map<Command, Map<String, Long>> COOLDOWN_MAP = new HashMap<>(); // K = Command, V = Map<userId, Timestamp>

    private CooldownHandler()
    {
        //Overrides the default, public, constructor
    }

    public static void addCooldown(String userID, Command command)
    {
        long cooldown = command.getCooldown();
        if (cooldown == 0)
        {
            return;
        }
        COOLDOWN_MAP.computeIfAbsent(command, k -> new HashMap<>()).put(userID, System.currentTimeMillis() + cooldown);
    }

    public static boolean isOnCooldown(String userID, Command command)
    {
        Map<String, Long> listEntry = COOLDOWN_MAP.get(command);
        if (listEntry == null)
        {
            return false;
        }
        long lastUsed = listEntry.get(userID);
        return lastUsed != 0 && System.currentTimeMillis() <= lastUsed;
    }

    public static long getCooldown(String userID, Command command)
    {
        return Math.abs(System.currentTimeMillis() - COOLDOWN_MAP.get(command).get(userID));
    }
}
