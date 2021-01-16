package org.igsq.igsqbot.minecraft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.ConfigOption;
import org.igsq.igsqbot.entities.Configuration;

public class MinecraftSync
{
	private final Minecraft minecraft;
	private final IGSQBot igsqbot;

	public MinecraftSync(Minecraft minecraft)
	{
		this.minecraft = minecraft;
		this.igsqbot = minecraft.getIGSQBot();
	}

	public void start()
	{
		if(!minecraft.getDatabaseHandler().isOnline())
		{
			igsqbot.getLogger().warn("Minecraft syncing stopped. Database not online.");
		}
		else
		{
			igsqbot.getTaskHandler().addRepeatingTask(this::sync, "minecraftSync", TimeUnit.SECONDS, 10);
		}
	}

	private void sync()
	{
		Guild guild = igsqbot.getShardManager().getGuildById(igsqbot.getConfig().getString(ConfigOption.HOMESERVER));

		if(guild == null)
		{
			close("Homeserver was null.");
		}
		else
		{
			guild.loadMembers(member ->
			{
				if(member.getUser().isBot())
				{
					return;
				}

				List<String> ranks = member.getRoles().stream()
						.map(Role::getId)
						.filter(role -> getRanks().containsKey(role))
						.map(role -> getRanks().get(role))
						.collect(Collectors.toList());

				if(ranks.isEmpty())
				{
					ranks.add("group.default");
				}

				Configuration configuration = igsqbot.getConfig();
				MinecraftUser minecraftUser = new MinecraftUser();
				minecraftUser.setId(member.getId());
				minecraftUser.setUsername(member.getUser().getAsTag());
				minecraftUser.setNickname(member.getEffectiveName());
				minecraftUser.setRole(ranks.get(0));

				minecraftUser.setFounder(hasRole(configuration.getString(ConfigOption.FOUNDER).split(","), member));
				minecraftUser.setBirthday(hasRole(configuration.getString(ConfigOption.BIRTHDAY).split(","), member));
				minecraftUser.setNitroboost(hasRole(configuration.getString(ConfigOption.NITROBOOST).split(","), member));
				minecraftUser.setSupporter(hasRole(configuration.getString(ConfigOption.SUPPORTER).split(","), member));
				minecraftUser.setDeveloper(hasRole(configuration.getString(ConfigOption.DEVELOPER).split(","), member));

				int isPresent = MinecraftUtils.isMemberPresent(minecraftUser, minecraft);
				if(isPresent == -1)
				{
					return;
				}

				if(isPresent == 1)
				{
					MinecraftUtils.updateMember(minecraftUser, minecraft);
				}
				else
				{
					MinecraftUtils.insertMember(minecraftUser, minecraft);
				}

			});
		}
	}

	private int hasRole(String role, Member member)
	{
		return member.getRoles().stream().map(Role::getId).filter(role::equals).count() == 1 ? 1 : 0;
	}

	private int hasRole(String[] roles, Member member)
	{
		for(String role : roles)
		{
			if(hasRole(role, member) == 1)
			{
				return 1;
			}
		}
		return 0;
	}

	private Map<String, String> getRanks()
	{
		Map<String, String> result = new HashMap<>();
		for(ConfigOption configOption : ConfigOption.getRanks())
		{
			try
			{
				for(String rank : igsqbot.getConfig().getString(configOption).split(","))
				{
					result.put(rank, configOption.getKey());
				}
			}
			catch(Exception exception)
			{
				close("An error occurred while loading the ranks.");
			}
		}
		return result;
	}

	public void close()
	{
		igsqbot.getTaskHandler().cancelTask("minecraftSync", false);
		igsqbot.getLogger().info("Minecraft syncing stopped.");
	}

	public void close(String message)
	{
		igsqbot.getTaskHandler().cancelTask("minecraftSync", false);
		igsqbot.getLogger().info("Minecraft syncing stopped. " + message);
	}
}
