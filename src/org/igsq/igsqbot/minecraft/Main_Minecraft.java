package org.igsq.igsqbot.minecraft;

import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.Common;

public class Main_Minecraft 
{
	public Main_Minecraft()
	{
		Sync_Minecraft.sync();
		Common.scheduler.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run() 
			{
				Sync_Minecraft.sync();
			} 		
		}, 1, 1,TimeUnit.HOURS);
	}
}
