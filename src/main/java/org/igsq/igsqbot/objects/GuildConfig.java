package org.igsq.igsqbot.objects;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuildConfig
{
	private final String guildId;
	private final JDA jda;

	public GuildConfig(String guildId, JDA jda)
	{
		this.guildId = guildId;
		this.jda = jda;
	}

	public GuildConfig(Guild guild, JDA jda)
	{
		this.guildId = guild.getId();
		this.jda = jda;
	}

	public TextChannel getVerificationChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".verificationchannel", "guild"))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".verificationchannel", "guild"));
		}
	}

	public TextChannel getReportChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".reportchannel", "guild"))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".reportchannel", "guild"));
		}
	}

	public TextChannel getWelcomeChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".welcomechannel", "guild"))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".welcomechannel", "guild"));
		}
	}

	public TextChannel getLogChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".textlog", "guild"))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".textlog", "guild"));
		}
	}

	public TextChannel getVoidLogChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".voicelog", "guild"))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".voicelog", "guild"));
		}
	}

	public List<Role> getServerJoinRoles()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".serverjoinroles", "guild"))
		{
			return new ArrayList<>();
		}
		else
		{
			return Arrays.stream(Yaml.getFieldString(guildId + ".serverjoinroles", "guild").split(" ")).map(jda::getRoleById).collect(Collectors.toList());
		}
	}

	public Role getVerifiedRole()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".verifiedrole", "guild"))
		{
			return null;
		}
		else
		{
			return jda.getRoleById(Yaml.getFieldString(guildId + ".verifiedrole", "guild"));
		}
	}

	public String getGuildPrefix()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".prefix", "guild"))
		{
			return Common.DEFAULT_BOT_PREFIX;
		}
		else
		{
			return Yaml.getFieldString(guildId + ".prefix", "guild").trim();
		}
	}

	public void setGuildPrefix(String prefix)
	{
		Yaml.updateField(guildId + ".prefix", "guild", " " + prefix);
	}
}
