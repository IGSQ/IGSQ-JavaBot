package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.entities.json.*;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Help;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Report;
import org.igsq.igsqbot.events.logging.MemberEventsLogging;
import org.igsq.igsqbot.events.logging.MessageEventsLogging;
import org.igsq.igsqbot.events.logging.VoiceEventsLogging;
import org.igsq.igsqbot.events.main.GuildEventsMain;
import org.igsq.igsqbot.events.main.MessageEventsMain;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.minecraft.MainMinecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class IGSQBot
{
	private static IGSQBot instance;

	private final LocalDateTime startTimestamp = LocalDateTime.now();
	private final Logger logger = LoggerFactory.getLogger(IGSQBot.class);
	private int readyShardID;
	private ShardManager shardManager;

	public static void main(String[] args)
	{
		instance = new IGSQBot();
		instance.start();
	}

	private void start()
	{
		Yaml.createFiles();
		Yaml.loadFile(Filename.ALL);
		Yaml.applyDefault();

		Json.createFiles();
		Json.applyDefaults();
		JsonGuildCache.getInstance().load();
		JsonPunishmentCache.getInstance().load();

		final JsonBotConfig jsonBotConfig = Json.get(JsonBotConfig.class, Filename.CONFIG);

		if(jsonBotConfig != null)
		{
			try
			{
				shardManager = DefaultShardManagerBuilder.createDefault(jsonBotConfig.getToken())
						.enableIntents(GatewayIntent.GUILD_MEMBERS)
						.setMemberCachePolicy(MemberCachePolicy.ALL)

						.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
						.setAutoReconnect(true)
						.setShardsTotal(-1)
						.addEventListeners(
								new MessageReactionAdd_Help(),
								new MessageReactionAdd_Report(),

								new MessageEventsMain(),
								new GuildEventsMain(),

								new VoiceEventsLogging(),
								new MessageEventsLogging(),
								new MemberEventsLogging()
						)

						.build();
				readyShardID = shardManager.getShards().get(shardManager.getShards().size() - 1).awaitReady().getShardInfo().getShardId();

				Database.startDatabase();
				MainMinecraft.startMinecraft(shardManager);

				instance.logger.info("IGSQBot started!");
				instance.logger.info("Account:         " + SelfUser.getAsTag() + " / " + SelfUser.getId());
				instance.logger.info("Total Shards:    " + shardManager.getShardsRunning());
				instance.logger.info("JDA Version:     " + JDAInfo.VERSION);
				instance.logger.info("IGSQBot Version: " + Constants.VERSION);
				instance.logger.info("Java Version:    " + System.getProperty("java.version"));

				TaskHandler.addRepeatingTask(() ->
				{
					for(JsonPunishment selectedPunishment: JsonPunishmentCache.getInstance().getAll())
					{
						if(selectedPunishment.isMuted() && System.currentTimeMillis() >= selectedPunishment.getMutedUntil())
						{
							selectedPunishment.setMuted(false);
							selectedPunishment.setMutedUntil(-1);

							Guild guild = instance.shardManager.getGuildById(selectedPunishment.getGuildId());
							if(guild != null)
							{
								for(String roleId : selectedPunishment.getRoles())
								{
									Role role = guild.getRoleById(roleId);
									if(role != null && guild.getSelfMember().canInteract(role))
									{
										guild.addRoleToMember(selectedPunishment.getUserId(), role).queue();
									}
								}
								selectedPunishment.setRoles(Collections.emptyList());
							}
						}
					}
				}, "muteCheck", TimeUnit.SECONDS, 5);

				TaskHandler.addRepeatingTask(() ->
				{
					Yaml.saveFileChanges(Filename.ALL);
					Yaml.loadFile(Filename.ALL);

					JsonPunishmentCache.getInstance().save();
					JsonPunishmentCache.getInstance().load();

					JsonGuildCache.getInstance().save();
					JsonGuildCache.getInstance().load();

				}, "yamlReload", TimeUnit.SECONDS, 5);

			}

			catch (IllegalArgumentException exception)
			{
				instance.logger.error("No login details provided! Please provide a token in the config.yml.");
			}
			catch (LoginException exception)
			{
				instance.logger.error("The provided token was invalid, please ensure you put a valid token in config.yml");
			}
			catch(Exception exception)
			{
				instance.logger.error("An unexpected exception occurred", exception);
			}
		}
		else
		{
			instance.logger.error("An error occurred when loading the config file.");
		}
	}


	public LocalDateTime getStartTimestamp()
	{
		return startTimestamp;
	}

	public ShardManager getShardManager()
	{
		return shardManager;
	}

	public int getReadyShardID()
	{
		return readyShardID;
	}

	public static IGSQBot getInstance()
	{
		return instance;
	}

	public static class SelfUser
	{
		private SelfUser()
		{
			//Overrides the default, public, constructor
		}
		private static final String id = IGSQBot.getInstance().getShardManager().getShardById(IGSQBot.getInstance().getReadyShardID()).getSelfUser().getId();
		private static final String tag = IGSQBot.getInstance().getShardManager().getShardById(IGSQBot.getInstance().getReadyShardID()).getSelfUser().getAsTag();

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
