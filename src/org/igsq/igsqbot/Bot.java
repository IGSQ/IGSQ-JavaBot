package org.igsq.igsqbot;

import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.commands.Main_Command;
import org.igsq.igsqbot.logging.Main_Logging;
import org.igsq.igsqbot.main.MessageDeleteEvent_Main;
import org.igsq.igsqbot.main.MessageReactionAddEvent_Main;
import org.igsq.igsqbot.main.MessageReceivedEvent_Main;
import org.igsq.igsqbot.minecraft.Main_Minecraft;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot
{
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
		
		Common.scheduler.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run() 
			{
					System.out.println("Cleaning Message Caches: Starting Now.");
					MessageCache.cleanCaches();
					System.out.println("Cleaning Message Caches: Complete.");
			} 		
    	}, 0, 6,TimeUnit.HOURS);
		
		try 
		{
			Common.jdaBuilder = JDABuilder.createDefault(Yaml.getFieldString("BOT.token", "config"));
			Common.jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
			Common.jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
			
			new Main_Command();
			new Main_Logging();
			
			new MessageReactionAddEvent_Main();
			new MessageDeleteEvent_Main();
			new MessageReceivedEvent_Main();
			
			Common.jda = Common.jdaBuilder.build();
			Common.self = Common.jda.getSelfUser();
			
			Common.jda.awaitReady();
			
			Yaml.applyDefault();
			
			new Main_Minecraft();
			new Database();
			
		}
		catch(Exception exception)
		{
			System.err.println("Bot Failed To Start!");
			exception.printStackTrace();
		}
	}
}
