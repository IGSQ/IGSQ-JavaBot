package org.igsq.igsqbot.entities.info;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import org.igsq.igsqbot.IGSQBot;

public class BotInfo
{
	private final IGSQBot igsqBot;

	public BotInfo(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	public Duration getUptime()
	{
		return Duration.between(igsqBot.getStartTimestamp(), LocalDateTime.now());
	}

	public String getJavaVersion()
	{
		return System.getProperty("java.version");
	}

	public String getJavaVendor()
	{
		return System.getProperty("java.vendor");
	}

	public long getMaxMemory()
	{
		return Runtime.getRuntime().maxMemory();
	}

	public long getFreeMemory()
	{
		return Runtime.getRuntime().freeMemory();
	}

	public long getTotalMemory()
	{
		return Runtime.getRuntime().totalMemory();
	}

	public long getAvailableProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
	}

	public long getTotalShards()
	{
		return igsqBot.getShardManager().getShardsTotal();
	}

	public long getThreadCount()
	{
		return ManagementFactory.getThreadMXBean().getThreadCount();
	}

	public long getTotalServers()
	{
		return igsqBot.getShardManager().getGuildCache().size();
	}

	public String getMemoryFormatted()
	{
		return (getTotalMemory() - getFreeMemory() >> 20) + "MB / " + (getMaxMemory() >> 20) + "MB";
	}
}
