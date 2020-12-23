package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.handlers.EventHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.minecraft.MainMinecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class IGSQBot
{
	private static final LocalDateTime START_TIMESTAMP = LocalDateTime.now();
	private static final Logger LOGGER = LoggerFactory.getLogger(IGSQBot.class);
	private static JDA jda;

	public static void main(String[] args)
	{
		Yaml.createFiles();
		Yaml.loadFile(Filename.ALL);
		Yaml.applyDefault();

		try
		{
			jda = JDABuilder.createDefault(Yaml.getFieldString("bot.token", Filename.CONFIG))
					.enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setMemberCachePolicy(MemberCachePolicy.ALL)

					.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
					.setEventManager(EventHandler.getEventManager())
					.build().awaitReady();

			TaskHandler.addRepeatingTask(() ->
			{
				Yaml.saveFileChanges(Filename.ALL);
				Yaml.loadFile(Filename.ALL);
			}, "yamlReload", TimeUnit.SECONDS, 30);

			Database.startDatabase();
			EventHandler.registerEvents();
			MainMinecraft.startMinecraft(jda);

			LOGGER.info("IGSQBot started!");
			LOGGER.info("Account:         " + jda.getSelfUser().getAsTag() + " / " + jda.getSelfUser().getId());
			LOGGER.info("JDA Version:     " + JDAInfo.VERSION);
			LOGGER.info("IGSQBot Version: " + Constants.VERSION);
			LOGGER.info("Java Version:    " + System.getProperty("java.version"));



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

	public static LocalDateTime getStartTimestamp()
	{
		return START_TIMESTAMP;
	}
}
