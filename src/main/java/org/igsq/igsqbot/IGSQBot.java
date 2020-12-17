package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.handlers.EventHandler;
import org.igsq.igsqbot.minecraft.MainMinecraft;
import org.igsq.igsqbot.objects.MessageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class IGSQBot
{
	private static final Logger LOGGER = LoggerFactory.getLogger(IGSQBot.class);
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

					.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
					.setEventManager(EventHandler.getEventManager())
					.build().awaitReady();

			Common.scheduler.scheduleAtFixedRate(() ->
			{
				Yaml.saveFileChanges("@all");
				Yaml.loadFile("@all");
			}, 0, 30,TimeUnit.SECONDS);

			Common.scheduler.scheduleAtFixedRate(MessageCache::cleanCaches, 0, 6,TimeUnit.HOURS);

			EventHandler.setEvents();
			MainMinecraft.startMinecraft();
			Database.startDatabase();
		}

		catch(Exception exception)
		{
			LOGGER.error("Fatal exception occurred when the bot tried to start.", exception);
		}
	}

	public static JDA getJDA()
	{
		return jda;
	}
}
