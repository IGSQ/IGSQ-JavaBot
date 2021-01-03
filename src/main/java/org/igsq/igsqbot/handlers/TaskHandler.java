package org.igsq.igsqbot.handlers;

import org.igsq.igsqbot.entities.BotTask;

import java.util.*;
import java.util.concurrent.*;

public class TaskHandler
{
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
	private final List<BotTask> TASKS = new ArrayList<>();
	private final List<UUID> currentUUIDs = new ArrayList<>();

	public BotTask addTask(Runnable task, TimeUnit unit, long time)
	{
		String taskName = getTaskName();
		BotTask botTask = new BotTask(scheduler.schedule(task, time, unit), taskName, time, unit);
		TASKS.add(botTask);
		scheduleDeletion(botTask);
		return botTask;
	}

	public BotTask addTask(Runnable task, String taskName, TimeUnit unit, long time)
	{
		BotTask botTask = new BotTask(scheduler.schedule(task, time, unit), taskName, time, unit);
		TASKS.add(botTask);
		scheduleDeletion(botTask);
		return botTask;
	}

	public BotTask addTask(Callable<?> task, String taskName, TimeUnit unit, long time)
	{
		BotTask botTask = new BotTask(scheduler.schedule(task, time, unit), taskName, time, unit);
		TASKS.add(botTask);
		scheduleDeletion(botTask);
		return botTask;
	}

	public BotTask addRepeatingTask(Runnable task, String taskName, long initialDelay, TimeUnit unit, long period)
	{
		BotTask botTask = new BotTask(scheduler.scheduleAtFixedRate(task, initialDelay, period, unit), taskName, period + initialDelay, unit);
		TASKS.add(botTask);
		return botTask;
	}

	public BotTask addRepeatingTask(Runnable task, String taskName, TimeUnit unit, long time)
	{
		return addRepeatingTask(task, taskName, 0, unit, time);
	}

	public BotTask addRepeatingTask(Runnable task, TimeUnit unit, long time)
	{
		return addRepeatingTask(task, "" + System.currentTimeMillis(), 0, unit, time);
	}

	public BotTask getTask(String taskName)
	{
		for(BotTask task : TASKS)
		{
			if(task.getName().equalsIgnoreCase(taskName))
			{
				return task;
			}
		}
		return null;
	}

	public boolean cancelTask(String taskName, boolean shouldInterrupt)
	{
		for(BotTask task : TASKS)
		{
			if(task.getName().equalsIgnoreCase(taskName))
			{
				return task.getTask().cancel(shouldInterrupt);
			}
		}
		return false;
	}

	public void close()
	{
		for(BotTask task : TASKS)
		{
			task.cancel(false);
		}
	}

	public String getTaskName()
	{
		UUID uuid = UUID.randomUUID();
		if(!currentUUIDs.contains(uuid))
		{
			currentUUIDs.add(uuid);
			return uuid.toString();
		}
		else
		{
			return getTaskName();
		}
	}

	public List<BotTask> getTASKS()
	{
		return TASKS;
	}

	private void scheduleDeletion(BotTask task)
	{
		scheduler.schedule(() -> TASKS.remove(task), task.getExpiresAt(), task.getUnit());
	}
}
