package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.handlers.TaskHandler;

import java.util.concurrent.TimeUnit;

public class MainMinecraft
{


	private MainMinecraft()
	{
		//Override the default, public, constructor
	}

	public static void startMinecraft()
	{
		TwoFAMinecraft.startTwoFA();
		new GuildMemberRemoveEvent_Minecraft();

		TaskHandler.addRepeatingTask(SyncMinecraft::sync, "minecraftSync", 0, TimeUnit.SECONDS, 10);
		TaskHandler.addRepeatingTask(SyncMinecraft::clean, "minecraftClean", 0, TimeUnit.HOURS, 6);
	}
}
