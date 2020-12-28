package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.commands.*;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.yaml.BotConfig;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.entities.yaml.Punishment;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Help;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Report;
import org.igsq.igsqbot.events.logging.MemberEventsLogging;
import org.igsq.igsqbot.events.logging.MessageEventsLogging;
import org.igsq.igsqbot.events.logging.VoiceEventsLogging;
import org.igsq.igsqbot.events.main.GuildEventsMain;
import org.igsq.igsqbot.events.main.MessageEventsMain;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.minecraft.MainMinecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

		final BotConfig botConfig = new BotConfig();
		try
		{
			shardManager = DefaultShardManagerBuilder.createDefault(botConfig.getToken())
					.enableIntents(GatewayIntent.GUILD_MEMBERS)
					.setMemberCachePolicy(MemberCachePolicy.ALL)

					.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
					.setAutoReconnect(true)
					.setShardsTotal(-1)
					.addEventListeners(
							new MessageEventsMain(),
							new GuildEventsMain(),

							new VoiceEventsLogging(),
							new MessageEventsLogging(),
							new MemberEventsLogging(),

							new MessageReactionAdd_Help(),
							new MessageReactionAdd_Report()
							)

					.build();

				shardManager.getShards().forEach(shard ->
				{
					try
					{
						shard.awaitReady();
						LOGGER.info("Shard " + shard.getShardInfo().getShardId() + " has loaded."); //TODO: note the ID thats ready for use in SelfUser
					}
					catch(Exception exception)
					{
						LOGGER.info("Shard " + shard.getShardInfo().getShardId() + " was interrupted during load.");
					}
				});


			final List<Command> commandList = new ArrayList<>();
			commandList.add(new AvatarCommand());
			commandList.add(new ClearCommand());
			commandList.add(new HelpCommand());
			commandList.add(new InviteCommand());
			commandList.add(new MockCommand());
			commandList.add(new ModhelpCommand());
			commandList.add(new MuteCommand());
			commandList.add(new PingCommand());
			commandList.add(new PollCommand());
			commandList.add(new PrefixCommand());
			commandList.add(new ReactionRoleCommand());
			commandList.add(new ReportCommand());
			commandList.add(new StealCommand());
			commandList.add(new SuggestionCommand());
			commandList.add(new UptimeCommand());
			commandList.add(new UwUCommand());
			commandList.add(new VerificationCommand());
			commandList.add(new WarnCommand());

			if(!botConfig.getPrivilegedUsers().isEmpty())
			{
				commandList.add(new ModuleCommand());
				commandList.add(new ShutdownCommand());
				commandList.add(new TestCommand());
			}
			if(Database.startDatabase())
			{
				commandList.add(new LinkCommand());
			}

			CommandHandler.setCommandMap(commandList);
			TaskHandler.addRepeatingTask(() -> Punishment.checkMutes(shardManager), "muteCheck", TimeUnit.SECONDS, 30);
			MainMinecraft.startMinecraft(shardManager);

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

		catch (IllegalArgumentException exception)
		{
			LOGGER.error("No login details provided! Please provide a token in the config.yml.");
		}
		catch (LoginException exception)
		{
			LOGGER.error("The provided token was invalid, please ensure you put a valid token in config.yml");
		}
	}

	public static LocalDateTime getStartTimestamp()
	{
		return START_TIMESTAMP;
	}

	public static ShardManager getShardManager()
	{
		return shardManager;
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
