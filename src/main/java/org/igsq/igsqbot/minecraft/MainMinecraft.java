package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.Common;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainMinecraft
{
	private static final ScheduledFuture<?> syncTask = Common.scheduler.scheduleAtFixedRate(SyncMinecraft::sync, 0, 10,TimeUnit.SECONDS);
	private static final ScheduledFuture<?> cleanTask = Common.scheduler.scheduleAtFixedRate(SyncMinecraft::clean, 0, 6,TimeUnit.HOURS);

	private MainMinecraft()
	{
		//Override the default, public, constructor
	}

	public static void startMinecraft()
	{
		TwoFAMinecraft.startTwoFA();
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
