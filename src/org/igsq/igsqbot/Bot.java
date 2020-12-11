package org.igsq.igsqbot;

import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.events.MessageReactionAddEvent_Help;
import org.igsq.igsqbot.events.MessageReactionAddEvent_Report;
import org.igsq.igsqbot.logging.GuildMemberJoinEvent_Logging;
import org.igsq.igsqbot.logging.GuildMemberRemoveEvent_Logging;
import org.igsq.igsqbot.logging.MessageBulkDeleteEvent_Logging;
import org.igsq.igsqbot.logging.MessageDeleteEvent_Logging;
import org.igsq.igsqbot.logging.MessageUpdateEvent_Logging;
import org.igsq.igsqbot.events.MessageDeleteEvent_Main;
import org.igsq.igsqbot.events.MessageReactionAddEvent_Main;
import org.igsq.igsqbot.events.MessageReceivedEvent_Main;
import org.igsq.igsqbot.minecraft.Main_Minecraft;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.objects.MessageCache;


public class Bot
{
	public static void main(String[] args)
	{
		Yaml.createFiles();
		Yaml.loadFile("@all");
		
		try 
		{
			Common.setJdaBuilder(JDABuilder.createDefault(Yaml.getFieldString("BOT.token", "config")));
			Common.getJdaBuilder().enableIntents(GatewayIntent.GUILD_MEMBERS);
			Common.getJdaBuilder().setMemberCachePolicy(MemberCachePolicy.ALL);
			Common.setJda(Common.getJdaBuilder().build().awaitReady());
			
			Common.getJda().addEventListener(
					new MessageReactionAddEvent_Main(),
					new MessageDeleteEvent_Main(),
					new MessageReceivedEvent_Main(),
					
					new MessageReactionAddEvent_Help(),
					new MessageReactionAddEvent_Report(),
					//new MessageReactionAddEvent_Verification(),
					
					new GuildMemberJoinEvent_Logging(),
					new GuildMemberRemoveEvent_Logging(),
					new MessageBulkDeleteEvent_Logging(),
					new MessageDeleteEvent_Logging(),
					new MessageUpdateEvent_Logging()
					);

			Common.scheduler.scheduleAtFixedRate(() ->
			{
				Yaml.saveFileChanges("@all");
				Yaml.loadFile("@all");
			}, 0, 30,TimeUnit.SECONDS);

			Common.scheduler.scheduleAtFixedRate(() ->
			{
				System.out.println("Cleaning Message Caches: Starting.");
				MessageCache.cleanCaches();
				System.out.println("Cleaning Message Caches: Complete.");
			}, 0, 6,TimeUnit.HOURS);

			Main_Minecraft.startMinecraft();
			Database.startDatabase();
			Yaml.applyDefault();
		}
		catch(Exception exception)
		{
			System.err.println("Bot Failed To Start!");
			exception.printStackTrace();
		}
	}
}
