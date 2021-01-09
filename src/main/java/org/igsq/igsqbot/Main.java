package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDAInfo;
import org.igsq.igsqbot.entities.database.Mute;
import org.igsq.igsqbot.util.DatabaseUtils;

import javax.security.auth.login.LoginException;
import java.util.concurrent.TimeUnit;

public class Main
{
	public static void main(String[] args)
	{
		IGSQBot bot = new IGSQBot();

		try
		{
			bot.build();
			bot.getJDA();
			bot.getDatabaseManager().createTables();
			bot.registerGuilds();
			bot.getMinecraft();
			bot.getCommandHandler();
		}
		catch(LoginException exception)
		{
			bot.getLogger().error("The provided token was invalid, please ensure you put a valid token in CONFIG.json", exception);
		}
		catch(IllegalArgumentException exception)
		{
			bot.getLogger().error("A provided value was invalid, please double check the values in CONFIG.json", exception);
		}
		catch(Exception exception)
		{
			bot.getLogger().error("An unhandled exception occurred", exception);
		}

		bot.getLogger().info("  ___ ___ ___  ___  ___      _     ___ _            _          _ ");
		bot.getLogger().info(" |_ _/ __/ __|/ _ \\| _ ) ___| |_  / __| |_ __ _ _ _| |_ ___ __| |");
		bot.getLogger().info("  | | (_ \\__ \\ (_) | _ \\/ _ \\  _| \\__ \\  _/ _` | '_|  _/ -_) _` |");
		bot.getLogger().info(" |___\\___|___/\\__\\_\\___/\\___/\\__| |___/\\__\\__,_|_|  \\__\\___\\__,_|");
		bot.getLogger().info("");
		bot.getLogger().info("Account:         " + bot.getSelfUser().getAsTag() + " / " + bot.getSelfUser().getId());
		bot.getLogger().info("Total Shards:    " + bot.getShardManager().getShardsRunning());
		bot.getLogger().info("JDA Version:     " + JDAInfo.VERSION);
		bot.getLogger().info("IGSQBot Version: " + Constants.VERSION);
		bot.getLogger().info("JVM Version:     " + System.getProperty("java.version"));

		bot.getTaskHandler().addRepeatingTask(() -> DatabaseUtils.getExpiredMutes(bot).forEach(mute -> Mute.removeMuteById(mute.getUserid(), bot)), TimeUnit.SECONDS, 15);
	}
}
