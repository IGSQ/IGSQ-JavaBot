package org.igsq.igsqbot.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class TaskHandler
{
	private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static final Map<String, ScheduledFuture<?>> TASK_MAP = new HashMap<>();

	private TaskHandler()
	{
		//Overrides the default, public, constructor
	}

	public static ScheduledFuture<?> addTask(Runnable task, TimeUnit unit, long time)
	{
		String taskName = "" + System.currentTimeMillis();
		TASK_MAP.putIfAbsent(taskName, scheduler.schedule(task, time, unit));
		return TASK_MAP.get(taskName);
	}

	public static ScheduledFuture<?> addTask(Runnable task, String taskName, TimeUnit unit, long time)
	{
		TASK_MAP.putIfAbsent(taskName, scheduler.schedule(task, time, unit));
		return TASK_MAP.get(taskName);
	}

	public static ScheduledFuture<?> addTask(Callable<?> task, TimeUnit unit, long time)
	{
		String taskName = "" + System.currentTimeMillis();
		FutureTask<?> calledTask = new FutureTask<>(task);
		TASK_MAP.putIfAbsent(taskName, scheduler.schedule(calledTask, time, unit));
		return TASK_MAP.get(taskName);
	}

	public static ScheduledFuture<?> addRepeatingTask(Runnable task, String taskName, long initialDelay, TimeUnit unit, long period)
	{
		TASK_MAP.putIfAbsent(taskName, scheduler.scheduleAtFixedRate(task, initialDelay, period, unit));
		return TASK_MAP.get(taskName);
	}

	public static ScheduledFuture<?> addRepeatingTask(Runnable task, String taskName, TimeUnit unit, long time)
	{
		return addRepeatingTask(task, taskName, 0, unit, time);
	}

	public static ScheduledFuture<?> addRepeatingTask(Runnable task, TimeUnit unit, long time)
	{
		return addRepeatingTask(task, "" + System.currentTimeMillis(), 0, unit, time);
	}

	public static ScheduledFuture<?> getTask(String taskName)
	{
		return TASK_MAP.get(taskName);
	}

	public static boolean cancelTask(String taskName)
	{
		return cancelTask(taskName, false);
	}

	public static boolean cancelTask(String taskName, boolean shouldInterrupt)
	{
		ScheduledFuture<?> task = TASK_MAP.get(taskName);
		if(task == null) return false;
		else
		{
			task.cancel(shouldInterrupt);
			return true;
		}
	}

	public static void close()
	{
		for(ScheduledFuture<?> task : TASK_MAP.values())
		{
			task.cancel(false);
		}
	}
}
