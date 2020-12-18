package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.JDA;
import org.igsq.igsqbot.handlers.TaskHandler;

import java.util.concurrent.TimeUnit;

public class MainMinecraft
{


	private MainMinecraft()
	{
		//Override the default, public, constructor
	}

	public static void startMinecraft(JDA jda)
	{
		SyncMinecraft.startSync(jda);
		TwoFAMinecraft.startTwoFA();
		TaskHandler.addRepeatingTask(SyncMinecraft::sync, "minecraftSync", 0, TimeUnit.SECONDS, 10);
		TaskHandler.addRepeatingTask(SyncMinecraft::clean, "minecraftClean", 0, TimeUnit.HOURS, 6);
		new GuildMemberRemoveEvent_Minecraft();
	}
}
