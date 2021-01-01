package org.igsq.igsqbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.JsonBotConfig;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Help;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Report;
import org.igsq.igsqbot.events.logging.MemberEventsLogging;
import org.igsq.igsqbot.events.logging.MessageEventsLogging;
import org.igsq.igsqbot.events.logging.VoiceEventsLogging;
import org.igsq.igsqbot.events.main.GuildEventsMain;
import org.igsq.igsqbot.events.main.MessageEventsMain;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.minecraft.Minecraft;
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
	private Minecraft minecraft;
	private Database database;
	private JDA readyShard;

	public void build() throws LoginException
	{
		JsonBotConfig jsonBotConfig = Json.get(JsonBotConfig.class, Filename.CONFIG);
		if(jsonBotConfig == null)
		{
			throw new NullPointerException("Json was null.");
		}
		this.shardManager = DefaultShardManagerBuilder.createDefault(jsonBotConfig.getToken())
				.enableIntents(GatewayIntent.GUILD_MEMBERS)
				.setMemberCachePolicy(MemberCachePolicy.ALL)

				.setActivity(Activity.watching("IGSQ | v0.0.1 | igsq.org"))
				.setAutoReconnect(true)
				.setShardsTotal(-1)
				.addEventListeners(
						new MessageReactionAdd_Help(this),
						new MessageReactionAdd_Report(this),

						new MessageEventsMain(this),
						new GuildEventsMain(this),

						new VoiceEventsLogging(this),
						new MessageEventsLogging(this),
						new MemberEventsLogging(this)
				)
				.build();
	}

	public void getReadyShard() throws InterruptedException
	{
		if(shardManager == null)
		{
			throw new UnsupportedOperationException("Cannot get ready shard without a shard manager.");
		}
		else if(readyShard == null)
		{
			try
			{
				readyShard = shardManager.getShards().get(shardManager.getShards().size() - 1).awaitReady();
			}
			catch(InterruptedException exception)
			{
				throw new InterruptedException("The bot was interrupted during startup");
			}
		}
	}

	public SelfUser getSelfUser()
	{
		if(readyShard == null)
		{
			throw new UnsupportedOperationException("No ready shard present.");
		}
		return readyShard.getSelfUser();
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

	public Database getDatabase()
	{
		if(database == null)
		{
			database = new Database(this);
		}
		return database;
	}
}
