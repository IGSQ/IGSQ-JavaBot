package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.entities.yaml.Punishment;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Help;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Report;
import org.igsq.igsqbot.events.logging.*;
import org.igsq.igsqbot.events.main.*;
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
	private static ShardManager shardManager;

	public static void main(String[] args)
	{
		Yaml.createFiles();
		Yaml.loadFile(Filename.ALL);
		Yaml.applyDefault();

		try
		{
			shardManager = DefaultShardManagerBuilder.createDefault(Yaml.getFieldString("bot.token", Filename.CONFIG))
					.enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setMemberCachePolicy(MemberCachePolicy.ALL)

					.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
					.setAutoReconnect(true)
					.addEventListeners(
							new MessageReactionAdd_Main(),
							new MessageDelete_Main(),
							new MessageReceived_Main(),
							new MessageReactionRemove_Main(),

							new GuildLeave_Main(),
							new UnavailableGuildLeave_Main(),

							new MessageReactionAdd_Help(),
							new MessageReactionAdd_Report(),

							new GuildMemberJoin_Logging(),
							new GuildMemberRemove_Logging(),

							new GuildVoiceJoin_Logging(),
							new GuildVoiceLeave_Logging(),
							new GuildVoiceMove_Logging(),

							new MessageBulkDelete_Logging(),
							new MessageDelete_Logging(),
							new MessageUpdate_Logging())
					.setShardsTotal(-1)
					.build();

				shardManager.getShards().forEach(shard ->
				{
					try
					{
						shard.awaitReady();
					}
					catch(Exception exception)
					{
						LOGGER.info("A shard was interrupted during startup.");
					}
				});

			TaskHandler.addRepeatingTask(() -> Punishment.checkMutes(shardManager), "muteCheck", TimeUnit.MINUTES, 1);
			MainMinecraft.startMinecraft(shardManager);
			Database.startDatabase();


			LOGGER.info("IGSQBot started!");
			LOGGER.info("Account:         " + SelfUser.getAsTag() + " / " + SelfUser.getId());
			LOGGER.info("Total Shards:    " + shardManager.getShardsRunning());
			LOGGER.info("JDA Version:     " + JDAInfo.VERSION);
			LOGGER.info("IGSQBot Version: " + Constants.VERSION);
			LOGGER.info("Java Version:    " + System.getProperty("java.version"));

			TaskHandler.addRepeatingTask(() ->
			{
				Yaml.saveFileChanges(Filename.ALL);
				Yaml.loadFile(Filename.ALL);
			}, "yamlReload", TimeUnit.SECONDS, 30);
		}
		catch(Exception exception)
		{
			LOGGER.error("A fatal exception occurred when the bot tried to start.", exception);
		}
	}

	public static ShardManager getShardManager()
	{
		return shardManager;
	}

	public static LocalDateTime getStartTimestamp()
	{
		return START_TIMESTAMP;
	}

	public static class SelfUser
	{
		private SelfUser()
		{
			//Overrides the default, public, constructor
		}

		private static final String id = IGSQBot.shardManager.getShardById(0).getSelfUser().getId();
		private static final String tag = IGSQBot.shardManager.getShardById(0).getSelfUser().getAsTag();

		public static String getId()
		{
			return id;
		}

		public static String getAsTag()
		{
			return tag;
		}
	}
}
