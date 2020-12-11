package org.igsq.igsqbot.minecraft;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;

public class Main_Minecraft 
{
	private static final ScheduledFuture<?> syncTask = Common.scheduler.scheduleAtFixedRate(Sync_Minecraft::sync, 0, 10,TimeUnit.SECONDS);
	private static final ScheduledFuture<?> cleanTask = Common.scheduler.scheduleAtFixedRate(Sync_Minecraft::clean, 0, 6,TimeUnit.HOURS);

	private Main_Minecraft()
	{
		//Override the default, public, constructor
	}

	public static void startMinecraft()
	{
		TwoFA_Minecraft.startTwoFA();
		new GuildMemberRemoveEvent_Minecraft();
	}

	public static void cancelSync()
	{
		syncTask.cancel(false);
	}

	public static void cancelClean()
	{
		cleanTask.cancel(false);
	}
}
