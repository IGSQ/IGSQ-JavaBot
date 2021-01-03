package org.igsq.igsqbot.entities;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BotTask
{
	private final ScheduledFuture<?> task;
	private final String name;
	private final long expiresAt;
	private final TimeUnit unit;

	public TimeUnit getUnit()
	{
		return unit;
	}

	public BotTask(ScheduledFuture<?> task, String name, long expiresAt, TimeUnit timeUnit)
	{
		this.task = task;
		this.name = name;
		this.expiresAt = expiresAt;
		this.unit = timeUnit;
	}

	public ScheduledFuture<?> getTask()
	{
		return task;
	}

	public String getName()
	{
		return name;
	}

	public long getExpiresAt()
	{
		return expiresAt;
	}

	public void cancel(boolean shouldInterrupt)
	{
		task.cancel(shouldInterrupt);
	}
}
