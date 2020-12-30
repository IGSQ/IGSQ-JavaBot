package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.sharding.ShardManager;
import org.igsq.igsqbot.handlers.TaskHandler;

import java.util.concurrent.TimeUnit;

public class MainMinecraft
{


	private MainMinecraft()
	{
		//Override the default, public, constructor
	}

	public static void startMinecraft(ShardManager shardManager)
	{
		SyncMinecraft.getInstance().start(shardManager);
		TwoFAMinecraft.start();
		TaskHandler.addRepeatingTask(() -> SyncMinecraft.getInstance().sync(), "minecraftSync", 0, TimeUnit.SECONDS, 10);
		TaskHandler.addRepeatingTask(() -> SyncMinecraft.getInstance().clean(), "minecraftClean", 0, TimeUnit.HOURS, 6);
	}
}
