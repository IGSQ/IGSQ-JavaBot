package org.igsq.igsqbot;

import java.awt.*;
import java.util.List;

public class Constants
{
	public static final String SUCCESS = "<:igsqTick:788476500012695582>";
	public static final String FAILURE = "<:igsqCross:788476443885174834>";
	public static final String DEFAULT_BOT_PREFIX = ".";

	public static final List<String> THUMB_REACTIONS = List.of("U+1F44D", "U+1F44E");
	public static final List<String> TICK_REACTIONS = List.of("U+2705", "U+274E");

	public static final Color IGSQ_PURPLE = new Color(104, 89, 133);

	public static String VERSION = "0.0.1";
	private Constants()
	{
		//Overrides the default, public, constructor
	}
}
