package org.igsq.igsqbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.Config;
import org.igsq.igsqbot.entities.ConfigOption;
import org.igsq.igsqbot.events.logging.MemberEventsLogging;
import org.igsq.igsqbot.events.logging.MessageEventsLogging;
import org.igsq.igsqbot.events.logging.VoiceEventsLogging;
import org.igsq.igsqbot.events.main.GuildEventsMain;
import org.igsq.igsqbot.events.main.MessageEventsMain;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.handlers.DatabaseHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.util.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IGSQBot
{
	private final Logger logger = LoggerFactory.getLogger(IGSQBot.class);
	private final LocalDateTime startTimestamp = LocalDateTime.now();
	private final List<EmbedBuilder> helpPages = new ArrayList<>();
	private CommandHandler commandHandler;
	private ShardManager shardManager;
	private TaskHandler taskHandler;
	private DatabaseHandler databaseHandler;
	private Minecraft minecraft;
	private Config config;
	private JDA jda;

	public void build() throws LoginException
	{
		this.shardManager = DefaultShardManagerBuilder
				.create(getConfig().getOption(ConfigOption.TOKEN),
						GatewayIntent.GUILD_MEMBERS,

						GatewayIntent.DIRECT_MESSAGES,
						GatewayIntent.DIRECT_MESSAGE_REACTIONS,

						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_MESSAGE_REACTIONS,
						GatewayIntent.GUILD_VOICE_STATES)

				.disableCache(
						CacheFlag.ACTIVITY,
						CacheFlag.EMOTE,
						CacheFlag.CLIENT_STATUS,
						CacheFlag.ROLE_TAGS,
						CacheFlag.MEMBER_OVERRIDES)

				.setMemberCachePolicy(MemberCachePolicy.NONE)
				.setShardsTotal(-1)

				.addEventListeners(
						new MessageEventsMain(this),
						new GuildEventsMain(this),

						new VoiceEventsLogging(this),
						new MessageEventsLogging(this),
						new MemberEventsLogging(this)
				)

				.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
				.build();
	}

	public JDA getJDA()
	{
		if(shardManager == null)
		{
			throw new UnsupportedOperationException("Cannot get ready shard without a shard manager.");
		}
		else if(jda == null)
		{
			try
			{
				jda = shardManager.getShards().get(shardManager.getShards().size() - 1).awaitReady();
			}
			catch(InterruptedException exception)
			{
				getLogger().error("The bot was interrupted at startup.", exception);
			}
		}
		return jda;
	}

	public SelfUser getSelfUser()
	{
		if(jda == null)
		{
			throw new UnsupportedOperationException("No ready shard present.");
		}
		return jda.getSelfUser();
	}

	public void registerGuilds()
	{
		if(shardManager == null)
		{
			throw new UnsupportedOperationException("Cannot register guilds without a shard manager.");
		}
		for(Guild guild : shardManager.getGuilds())
		{
			DatabaseUtils.registerGuild(guild, this);
		}
	}

	public LocalDateTime getStartTimestamp()
	{
		return startTimestamp;
	}

	public List<EmbedBuilder> getHelpPages()
	{
		if(helpPages.isEmpty())
		{
			List<Command> commands = new ArrayList<>();
			for(Command cmd : getCommandHandler().getCommandMap().values())
			{
				if(!commands.contains(cmd))
				{
					commands.add(cmd);
				}
			}

			EmbedBuilder embedBuilder = new EmbedBuilder();
			int fieldCount = 0;
			int page = 1;
			for(Command cmd : commands)
			{
				if(fieldCount < 6)
				{
					fieldCount++;
					embedBuilder.setTitle("Help page: " + page);
					embedBuilder.addField(cmd.getName(), cmd.getDescription() + "\n**" + cmd.getAliases().get(0) + "**`" + cmd.getSyntax() + "`", fieldCount % 2 == 0);
					embedBuilder.setColor(Constants.IGSQ_PURPLE);

				}
				else
				{
					helpPages.add(embedBuilder);
					embedBuilder = new EmbedBuilder();
					fieldCount = 0;
					page++;
				}
			}
		}
		return helpPages;
	}

	public ShardManager getShardManager()
	{
		if(shardManager == null)
		{
			throw new UnsupportedOperationException("Shardmanager is not built.");
		}
		return shardManager;
	}

	public Minecraft getMinecraft()
	{
		if(minecraft == null)
		{
			minecraft = new Minecraft(this);
		}
		return minecraft;
	}

	public Config getConfig()
	{
		if(config == null)
		{
			config = new Config(this);
		}
		return config;
	}

	public CommandHandler getCommandHandler()
	{
		if(commandHandler == null)
		{
			commandHandler = new CommandHandler(this);
		}
		return commandHandler;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public TaskHandler getTaskHandler()
	{
		if(taskHandler == null)
		{
			taskHandler = new TaskHandler();
		}
		return taskHandler;
	}

	public DatabaseHandler getDatabaseManager()
	{
		if(databaseHandler == null)
		{
			databaseHandler = new DatabaseHandler(this);
		}
		return databaseHandler;
	}
}
