package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.handlers.EventHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.minecraft.MainMinecraft;
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

			TaskHandler.addRepeatingTask(() ->
			{
				Yaml.saveFileChanges("@all");
				Yaml.loadFile("@all");
			}, "yamlReload", TimeUnit.SECONDS, 30);

			Database.startDatabase();
			EventHandler.setEvents();
			MainMinecraft.startMinecraft(jda);

		}
		catch(Exception exception)
		{
			LOGGER.error("A fatal exception occurred when the bot tried to start.", exception);
		}
	}

	public static JDA getJDA()
	{
		return jda;
	}
}
