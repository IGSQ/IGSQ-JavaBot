package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.events.*;
import org.igsq.igsqbot.logging.*;
import org.igsq.igsqbot.minecraft.Main_Minecraft;
import org.igsq.igsqbot.objects.MessageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class IGSQBot
{
	private static JDA jda;
	private static final Logger LOGGER = LoggerFactory.getLogger(IGSQBot.class);

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

			Common.scheduler.scheduleAtFixedRate(MessageCache::cleanCaches, 0, 6,TimeUnit.HOURS);

			Main_Minecraft.startMinecraft();
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
