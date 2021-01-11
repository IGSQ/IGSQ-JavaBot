package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.ConfigOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
			igsqbot.getLogger().warn("Minecraft syncing stopped.");
		}
		else
		{
			igsqbot.getTaskHandler().addRepeatingTask(this::sync, "minecraftSync", TimeUnit.SECONDS, 10);
		}
	}

	private void sync()
	{
		Guild guild = igsqbot.getShardManager().getGuildById(igsqbot.getConfig().getOption(ConfigOption.HOMESERVER));

		if(guild == null)
		{
			close();
		}
		else
		{
			guild.loadMembers(member ->
			{
				List<String> ranks = member.getRoles().stream()
						.map(Role::getIdLong)
						.map(role -> getRanks().get(role))
						.collect(Collectors.toList());

				if(!ranks.isEmpty())
				{
					String id = member.getId();
					String username = member.getUser().getAsTag();
					String nickname = member.getEffectiveName();
					String role = ranks.get(0);
				}
			});
		}
	}

	private int hasRole(long role, Member member)
	{
		return member.getRoles().stream().map(Role::getIdLong).filter(id -> role == id).count() == 1 ? 1 : 0;
	}

	private int hasRole(List<Long> roles, Member member)
	{
		for(Long role : roles)
		{
			if(hasRole(role, member) == 1)
			{
				return 1;
			}
		}
		return 0;
	}

	private Map<Long, String> getRanks()
	{
		Map<Long, String> result = new HashMap<>();
		for(ConfigOption configOption : ConfigOption.getMinecraftOptions())
		{
			try
			{
				result.put(Long.parseLong(igsqbot.getConfig().getOption(configOption)), configOption.getKey());
			}
			catch(Exception exception)
			{
				close();
			}
		}
		return result;
	}

	public void close()
	{
		igsqbot.getTaskHandler().cancelTask("minecraftSync", false);
		igsqbot.getLogger().info("Minecraft syncing stopped.");
	}
}
