package org.igsq.igsqbot;

import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.JsonGuildCache;
import org.igsq.igsqbot.entities.json.JsonPunishment;
import org.igsq.igsqbot.entities.json.JsonPunishmentCache;
import org.igsq.igsqbot.util.JsonUtils;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Main
{
	public static void main(String[] args)
	{
		IGSQBot bot = new IGSQBot();

		Yaml.createFiles();
		Yaml.loadFile(Filename.ALL);
		Yaml.applyDefault();

		Json.createFiles();
		Json.applyDefaults();

		JsonGuildCache.getInstance().load();
		JsonPunishmentCache.getInstance().load();

		try
		{
			bot.build();
			bot.getReadyShard();
			bot.getDatabase();
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
		catch(InterruptedException exception)
		{
			bot.getLogger().error("The bot was interrupted during launch.", exception);
		}
		catch(Exception exception)
		{
			bot.getLogger().error("An unhandled exception occurred", exception);
		}

		bot.getLogger().info("IGSQBot started!");
		bot.getLogger().info("Account:         " + bot.getSelfUser().getAsTag() + " / " + bot.getSelfUser().getId());
		bot.getLogger().info("Total Shards:    " + bot.getShardManager().getShardsRunning());
		bot.getLogger().info("JDA Version:     " + JDAInfo.VERSION);
		bot.getLogger().info("IGSQBot Version: " + Constants.VERSION);
		bot.getLogger().info("Java Version:    " + System.getProperty("java.version"));

		bot.getTaskHandler().addRepeatingTask(() ->
		{
			Yaml.saveFileChanges(Filename.ALL);
			Yaml.loadFile(Filename.ALL);

			JsonPunishmentCache.getInstance().reload();
			JsonGuildCache.getInstance().reload();

		}, "fileReload", TimeUnit.SECONDS, 5);

		bot.getTaskHandler().addRepeatingTask(() ->
		{
			for(JsonPunishment selectedPunishment : JsonUtils.getExpiredMutes())
			{
				selectedPunishment.setMuted(false);
				selectedPunishment.setMutedUntil(-1);

				Guild guild = bot.getShardManager().getGuildById(selectedPunishment.getGuildId());
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
		}, "muteCheck", TimeUnit.SECONDS, 30);
	}
}
