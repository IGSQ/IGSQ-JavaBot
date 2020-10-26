package org.igsq.igsqbot;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.commands.Main_Command;

import net.dv8tion.jda.api.JDABuilder;

public class Bot
{
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static Random random = new Random();
	public static void main(String[] args)
	{
		Yaml.createFiles();
		Yaml.loadFile("@all");
		Yaml.applyDefault();
		
		scheduler.scheduleAtFixedRate(new Runnable()
		{

			@Override
			public void run() 
			{
					Yaml.saveFileChanges("@all");
					Yaml.loadFile("@all");
			} 		
    	}, 1, 30,TimeUnit.SECONDS);
		
		new Database();
		
		try 
		{
			Common.jdaBuilder = JDABuilder.createDefault(Yaml.getFieldString("BOT.token", "config"));
			Common.jdaBuilder.addEventListeners(new Main_Command());
			
			Common.jda = Common.jdaBuilder.build();
			Common.self = Common.jda.getSelfUser();
			
			Common.jda.awaitReady();
			Common.sendEmbed(Common.STARTUP_MESSAGES[random.nextInt(Common.STARTUP_MESSAGES.length)], Common.jda.getTextChannelById("769356662896984090"),Color.GREEN);
			
		}
		catch(Exception exception)
		{
			System.err.println("Bot Failed To Start!");
		}
		
	}
}
