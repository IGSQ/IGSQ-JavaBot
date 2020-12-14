package org.igsq.igsqbot;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Common
{
	private Common()
	{
		//Overrides the default, public constructor
	}

	public static final String BOT_PREFIX = ".";

	public static final List<String> THUMB_REACTIONS = List.of("U+1F44D", "U+1F44E");
	public static final List<String> TICK_REACTIONS = List.of("U+2705", "U+274E");

	public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
}
