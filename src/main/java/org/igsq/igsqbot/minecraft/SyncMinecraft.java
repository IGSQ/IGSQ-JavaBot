package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Json;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.JsonBotConfig;
import org.igsq.igsqbot.entities.json.JsonMinecraft;

import java.util.concurrent.TimeUnit;

public class SyncMinecraft
{
	private final IGSQBot igsqBot;
	private Guild guild;

	public SyncMinecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		start();
	}

	private void start()
	{
		JsonBotConfig jsonBotConfig = Json.get(JsonBotConfig.class, Filename.CONFIG);
		if(jsonBotConfig == null)
		{
			igsqBot.getTaskHandler().cancelTask("minecraftSync");
			igsqBot.getLogger().error("An error occurred while reading JSON for Minecraft.");
			return;
		}
		if(!igsqBot.getDatabase().isOnline())
		{
			igsqBot.getTaskHandler().cancelTask("minecraftSync");
			igsqBot.getLogger().warn("Minecraft sync task stopped due to no Database connectivity being found.");
			return;
		}

		guild = igsqBot.getShardManager().getGuildById(jsonBotConfig.getServer());
		if(guild == null)
		{
			igsqBot.getTaskHandler().cancelTask("minecraftSync");
			igsqBot.getLogger().warn("Minecraft sync stopped due to invalid guild being defined in CONFIG.json.");
		}
		else
		{
			igsqBot.getLogger().info("Guild link established with guild: " + guild.getName() + ", starting sync.");
			igsqBot.getTaskHandler().addRepeatingTask(this::sync, "minecraftSync", 0, TimeUnit.SECONDS, 10);
		}
	}

	private void sync()
	{
		Role verifiedRole = guild.getRoleById(Yaml.getFieldString(guild.getId() + ".verifiedrole", Filename.GUILD));
		guild.loadMembers(selectedMember ->
		{
			if(!selectedMember.getUser().isBot() && (verifiedRole == null || selectedMember.getRoles().contains(verifiedRole)))
			{
				JsonMinecraft json = Json.get(JsonMinecraft.class, Filename.MINECRAFT);
				if(json != null)
				{
					String username = selectedMember.getUser().getAsTag();
					String nickname = selectedMember.getEffectiveName();
					String id = selectedMember.getId();
					String rank = MinecraftUtils.getRank(selectedMember);

					int supporter = MinecraftUtils.hasRole(json.getSupporter(), selectedMember) ? 1 : 0;
					int birthday = MinecraftUtils.hasRole(json.getBirthday(), selectedMember) ? 1 : 0;
					int developer = MinecraftUtils.hasRole(json.getDeveloper(), selectedMember) ? 1 : 0;
					int founder = MinecraftUtils.hasRole(json.getFounder(), selectedMember) ? 1 : 0;
					// int retired = hasRole(json.getRetired(), selectedMember)?1:0;
					int nitroboost = MinecraftUtils.hasRole(json.getNitroboost(), selectedMember) ? 1 : 0;

					boolean userExists = igsqBot.getDatabase().scalarCommand("SELECT COUNT(*) FROM discord_accounts WHERE id = '" + id + "';") > 0;

					if(userExists)
					{
						MinecraftUtils.updateUser(id, username, nickname, rank, supporter, birthday, developer, founder, nitroboost, igsqBot);
					}
					else
					{
						MinecraftUtils.addNewUser(id, username, nickname, rank, supporter, birthday, developer, founder, nitroboost, igsqBot);
					}
				}
			}
		});
	}
}
