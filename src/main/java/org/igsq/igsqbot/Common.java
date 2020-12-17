package org.igsq.igsqbot;

import java.time.LocalDateTime;
import java.util.List;

public class Common
{
	private Common()
	{
		//Overrides the default, public constructor
	}

	public static final String DEFAULT_BOT_PREFIX = ".";
	public static final List<String> THUMB_REACTIONS = List.of("U+1F44D", "U+1F44E");
	public static final List<String> TICK_REACTIONS = List.of("U+2705", "U+274E");
	public static final LocalDateTime START_TIMESTAMP = LocalDateTime.now();
}
