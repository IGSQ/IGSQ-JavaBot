package org.igsq.igsqbot;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
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

public class IGSQBot
{
	private static JDA jda;

	public static void main(String[] args)
	{
		Yaml.createFiles();
		Yaml.loadFile("@all");
		Yaml.applyDefault();
		
		try 
		{
			jda = JDABuilder.createDefault(Yaml.getFieldString("bot.token", "config"))
					.enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setMemberCachePolicy(MemberCachePolicy.ALL)

					.setActivity(Activity.watching("everything"))
					.build().awaitReady();

			jda.addEventListener(
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
		}

		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public static JDA getJDA()
	{
		return jda;
	}
}
