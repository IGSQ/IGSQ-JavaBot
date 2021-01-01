package org.igsq.igsqbot.handlers;

import java.util.*;
import java.util.concurrent.*;

public class TaskHandler
{
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
	private final Map<String, ScheduledFuture<?>> TASK_MAP = new HashMap<>();
	private final List<UUID> currentUUIDs = new ArrayList<>();

	public ScheduledFuture<?> addTask(Runnable task, TimeUnit unit, long time)
	{
		String taskName = getTaskName();
		TASK_MAP.putIfAbsent(taskName, scheduler.schedule(task, time, unit));
		scheduleDeletion(taskName, time, unit);
		return TASK_MAP.get(taskName);
	}

	public ScheduledFuture<?> addTask(Runnable task, String taskName, TimeUnit unit, long time)
	{
		TASK_MAP.putIfAbsent(taskName, scheduler.schedule(task, time, unit));
		scheduleDeletion(taskName, time, unit);
		return TASK_MAP.get(taskName);
	}

	public ScheduledFuture<?> addTask(Callable<?> task, TimeUnit unit, long time)
	{
		String taskName = getTaskName();
		FutureTask<?> calledTask = new FutureTask<>(task);
		TASK_MAP.putIfAbsent(taskName, scheduler.schedule(calledTask, time, unit));
		scheduleDeletion(taskName, time, unit);
		return TASK_MAP.get(taskName);
	}

	public ScheduledFuture<?> addTask(Callable<?> task, String taskName, TimeUnit unit, long time)
	{
		FutureTask<?> calledTask = new FutureTask<>(task);
		TASK_MAP.putIfAbsent(taskName, scheduler.schedule(calledTask, time, unit));
		scheduleDeletion(taskName, time, unit);
		return TASK_MAP.get(taskName);
	}

	public ScheduledFuture<?> addRepeatingTask(Runnable task, String taskName, long initialDelay, TimeUnit unit, long period)
	{
		TASK_MAP.putIfAbsent(taskName, scheduler.scheduleAtFixedRate(task, initialDelay, period, unit));
		scheduleDeletion(taskName, period + initialDelay, unit);
		return TASK_MAP.get(taskName);
	}

	public ScheduledFuture<?> addRepeatingTask(Runnable task, String taskName, TimeUnit unit, long time)
	{
		return addRepeatingTask(task, taskName, 0, unit, time);
	}

	public ScheduledFuture<?> addRepeatingTask(Runnable task, TimeUnit unit, long time)
	{
		return addRepeatingTask(task, "" + System.currentTimeMillis(), 0, unit, time);
	}

	public ScheduledFuture<?> getTask(String taskName)
	{
		return TASK_MAP.get(taskName);
	}

	public boolean cancelTask(String taskName)
	{
		return cancelTask(taskName, false);
	}

	public boolean cancelTask(String taskName, boolean shouldInterrupt)
	{
		ScheduledFuture<?> task = TASK_MAP.get(taskName);
		if(task == null) return false;
		else
		{
			task.cancel(shouldInterrupt);
			return true;
		}
	}

	public void close()
	{
		for(ScheduledFuture<?> task : TASK_MAP.values())
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

	private void scheduleDeletion(String taskName, long time, TimeUnit unit)
	{
		scheduler.schedule(() -> TASK_MAP.remove(taskName), time, unit);
	}
}
