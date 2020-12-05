package org.igsq.igsqbot.minecraft;

import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.Common;

public class Main_Minecraft 
{
	public Main_Minecraft()
	{
		Common.scheduler.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run() 
			{
				Sync_Minecraft.sync();
			} 		
		}, 0, 10,TimeUnit.SECONDS);
		
		Common.scheduler.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run() 
			{
				Sync_Minecraft.clean();
			} 		
		}, 0, 6,TimeUnit.HOURS);
		
		new GuildMemberRemoveEvent_Minecraft();
		new TwoFA_Minecraft();
	}
}
