package org.igsq.igsqbot.commands;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;

public class Cooldown_Handler
{
	private final String ID;
	private String[] activeCommands = {};
	private Cooldown_Handler me = this;
	
	ScheduledFuture<?> cooldownTask;
	Random random = new Random();
	
	public Cooldown_Handler(String id)
	{
		this.ID = id;
	}
	
	public void createCooldown(String command, int cooldown)
	{
		if(getCooldown(command) <= 0) 
		{
			Yaml.updateField(ID + ".cooldown." + command, "internal", cooldown);
			activeCommands = Common.append(activeCommands, command);
			updateTasks();
		}
	}
	
	public int getCooldown(String command)
	{
		return Yaml.getFieldInt(ID + ".cooldown." + command, "internal");
	}
	
	public boolean isCooldownActive(String command)
	{
		return Yaml.getFieldInt(ID + ".cooldown." + command, "internal") > 0;
	}
	
	private void updateTasks()
	{
		if(cooldownTask != null) cooldownTask.cancel(false);
		cooldownTask = Common.scheduler.scheduleAtFixedRate(new Runnable()
    	{
			@Override
			public void run() 
			{
				if(activeCommands.length == 0)
				{
					Main_Command.removeHandler(me);
					cooldownTask.cancel(false);
				}
				
				for(String selectedCommand : activeCommands)
				{
					if(isCooldownActive(selectedCommand))
					{
						Yaml.updateField(ID + ".cooldown." + selectedCommand, "internal", Yaml.getFieldInt(ID + ".cooldown." + selectedCommand, "internal") - 1);
					}
					else
					{
						activeCommands = Common.depend(activeCommands, selectedCommand);
					}
				}
			} 		
    	}, 0, 1, TimeUnit.MILLISECONDS);
	}
	
	public String getId() 
	{
		return ID;
	}
}
