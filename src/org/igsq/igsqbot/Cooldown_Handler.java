package org.igsq.igsqbot;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.commands.Main_Command;

import net.dv8tion.jda.api.entities.Guild;

public class Cooldown_Handler
{
	private final Guild GUILD;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private String[] activeCommands = {};
	private Cooldown_Handler me = this;
	
	ScheduledFuture<?> cooldownTask;
	Random random = new Random();
	
	public Cooldown_Handler(Guild guild)
	{
		this.GUILD = guild;
	}
	
	public void createCooldown(String command, int cooldown)
	{
		if(getCooldown(command) <= 0)
		{
			Yaml.updateField(GUILD.getId() + ".cooldown." + command, "internal", cooldown);
			activeCommands = Common.append(activeCommands, command);
			updateTasks();
		}
	}
	
	public int getCooldown(String command)
	{
		return Yaml.getFieldInt(GUILD.getId() + ".cooldown." + command, "internal");
	}
	
	public boolean isCooldownActive(String command)
	{
		return Yaml.getFieldInt(GUILD.getId() + ".cooldown." + command, "internal") > 0;
	}
	
	private void updateTasks()
	{
		if(cooldownTask != null) cooldownTask.cancel(false);
		cooldownTask = scheduler.scheduleAtFixedRate(new Runnable()
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
						Yaml.updateField(GUILD.getId() + ".cooldown." + selectedCommand, "internal", Yaml.getFieldInt(GUILD.getId() + ".cooldown." + selectedCommand, "internal") - 1);
					}
					else
					{
						activeCommands = Common.depend(activeCommands, selectedCommand);
					}
				}
			} 		
    	}, 0, 1, TimeUnit.MILLISECONDS);
	}
	
	public Guild getGuild() 
	{
		return GUILD;
	}
}
