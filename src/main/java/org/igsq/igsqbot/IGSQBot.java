package org.igsq.igsqbot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.ConfigOption;
import org.igsq.igsqbot.entities.Configuration;
import org.igsq.igsqbot.entities.database.Tempban;
import org.igsq.igsqbot.entities.info.BotInfo;
import org.igsq.igsqbot.events.command.ReportCommandReactionAdd;
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

public class IGSQBot extends ListenerAdapter
{
	private final Logger logger = LoggerFactory.getLogger(IGSQBot.class);
	private final LocalDateTime startTimestamp = LocalDateTime.now();
	private final List<EmbedBuilder> helpPages = new ArrayList<>();
	private DatabaseHandler databaseHandler;
	private CommandHandler commandHandler;
	private Configuration configuration;
	private ShardManager shardManager;
	private TaskHandler taskHandler;
	private Minecraft minecraft;
	private JDA jda;

	public void build() throws LoginException
	{
		this.shardManager = DefaultShardManagerBuilder
				.create(getConfig().getString(ConfigOption.TOKEN),
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
						new IGSQBot(),
						
						new MessageEventsMain(this),
						new GuildEventsMain(this),

						new ReportCommandReactionAdd(this),

						new VoiceEventsLogging(this),
						new MessageEventsLogging(this),
						new MemberEventsLogging(this)
				)

				.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
				.build();
	}

	@Override
	public void onReady(ReadyEvent event)
	{
		this.jda = event.getJDA();

		getDatabaseManager();
		registerGuilds(event.getJDA().getShardManager());
		getMinecraft();
		getCommandHandler();
		getStartTimestamp();

		getLogger().info("  ___ ___ ___  ___  ___      _     ___ _            _          _ ");
		getLogger().info(" |_ _/ __/ __|/ _ \\| _ ) ___| |_  / __| |_ __ _ _ _| |_ ___ __| |");
		getLogger().info("  | | (_ \\__ \\ (_) | _ \\/ _ \\  _| \\__ \\  _/ _` | '_|  _/ -_) _` |");
		getLogger().info(" |___\\___|___/\\__\\_\\___/\\___/\\__| |___/\\__\\__,_|_|  \\__\\___\\__,_|");
		getLogger().info("");
		getLogger().info("Account:         " + event.getJDA().getSelfUser().getAsTag() + " / " + event.getJDA().getSelfUser().getAsTag());
		getLogger().info("Total Shards:    " + BotInfo.getTotalShards(event.getJDA().getShardManager()));
		getLogger().info("Total Guilds:    " + BotInfo.getTotalServers(event.getJDA().getShardManager()));
		getLogger().info("JDA Version:     " + JDAInfo.VERSION);
		getLogger().info("IGSQBot Version: " + Constants.VERSION);
		getLogger().info("JVM Version:     " + BotInfo.getJavaVersion());

		getTaskHandler().addRepeatingTask(() -> DatabaseUtils.getExpiredMutes(this).forEach(mute -> Tempban.removeMuteById(mute.getUserid(), this)), TimeUnit.SECONDS, 15);
	}

	public SelfUser getSelfUser()
	{
		if(jda == null)
		{
			throw new UnsupportedOperationException("No ready shard present.");
		}
		return jda.getSelfUser();
	}

	public JDA getJDA()
	{
		return jda;
	}

	public void registerGuilds(ShardManager shardManager)
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
			for(int i = 0; i < commands.size(); i++)
			{
				Command cmd = commands.get(i);
				if(fieldCount < 6)
				{
					fieldCount++;
					embedBuilder.setTitle("Help page: " + page);
					embedBuilder.addField(cmd.getName(), cmd.getDescription() + "\n**" + cmd.getAliases().get(0) + "**`" + cmd.getSyntax() + "`", false);
					embedBuilder.setColor(Constants.IGSQ_PURPLE);
				}
				else
				{
					helpPages.add(embedBuilder);
					embedBuilder = new EmbedBuilder();
					fieldCount = 0;
					page++;
					i--;
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

	public Configuration getConfig()
	{
		if(configuration == null)
		{
			configuration = new Configuration(this);
		}
		return configuration;
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
