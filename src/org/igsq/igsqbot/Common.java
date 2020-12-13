package org.igsq.igsqbot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Common
{
	public static final String BOT_PREFIX = ".";
	public static final String COMMAND_PACKAGE = "org.igsq.igsqbot.commands";
	public static final Color IGSQ_PURPLE = new Color(104, 89, 133);

	public static final List<String> THUMB_REACTIONS = Collections.unmodifiableList(new ArrayList<>(Arrays.asList("U+1F44D","U+1F44E")));
	public static final List<String> TICK_REACTIONS = Collections.unmodifiableList(new ArrayList<>(Arrays.asList("U+2705","U+274E")));

	private static JDABuilder jdaBuilder;
	private static JDA jda;
	
	public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private Common()
	{
		//Overrides the default, public constructor
	}
	public static void setJda(JDA JDA)
	{
		jda = JDA;
	}

	public static JDA getJda()
	{
		return jda;
	}

	public static void setJdaBuilder(JDABuilder JDABuilder)
	{
		jdaBuilder = JDABuilder;
	}

	public static JDABuilder getJdaBuilder()
	{
		return jdaBuilder;
	}
}
