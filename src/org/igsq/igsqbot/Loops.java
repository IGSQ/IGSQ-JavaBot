package org.igsq.igsqbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.commands.Cooldown_Command;

public class Loops 
{
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public Loops()
	{
        fiveSeconds();
	}
	
	private void fiveSeconds()
	{
		scheduler.scheduleAtFixedRate(new Runnable() 
        {
			public void run() 
			{
				Cooldown_Command.CLEAR_COOLDOWN = 0;
			} 
        }, 5, 5 , TimeUnit.SECONDS);
	}
}
