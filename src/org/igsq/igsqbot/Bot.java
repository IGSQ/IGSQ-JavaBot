package org.igsq.igsqbot;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.commands.Main_Command;
import org.igsq.igsqbot.main.MessageDeleteEvent_Main;
import org.igsq.igsqbot.main.MessageReactionAddEvent_Main;
import org.igsq.igsqbot.main.MessageReceivedEvent_Main;

import net.dv8tion.jda.api.JDABuilder;

public class Bot
{
	private static Random random = new Random();
	public static void main(String[] args)
	{
		Yaml.createFiles();
		Yaml.loadFile("@all");
		
		Common.scheduler.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run() 
			{
					Yaml.saveFileChanges("@all");
					Yaml.loadFile("@all");
			} 		
    	}, 1, 30,TimeUnit.SECONDS);
		
		try 
		{
			Common.jdaBuilder = JDABuilder.createDefault(Yaml.getFieldString("BOT.token", "config"));
			
			new Main_Command();
		
			new MessageReactionAddEvent_Main();
			new MessageDeleteEvent_Main();
			new MessageReceivedEvent_Main();
			
			Common.jda = Common.jdaBuilder.build();
			Common.self = Common.jda.getSelfUser();
			
			Common.jda.awaitReady();
			new EmbedGenerator(Common.jda.getTextChannelById("769356662896984090")).text(Common.STARTUP_MESSAGES[random.nextInt(Common.STARTUP_MESSAGES.length)]).color(Color.GREEN);
			
			Yaml.applyDefault();
			new Database();
			
		}
		catch(Exception exception)
		{
			System.err.println("Bot Failed To Start!");
		}
	}
}
