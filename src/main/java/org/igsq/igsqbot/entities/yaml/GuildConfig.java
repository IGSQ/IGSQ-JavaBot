package org.igsq.igsqbot.entities.yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

	public void setVerificationChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".verificationchannel", "guild", channel.getId());
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

	public void setReportChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".reportchannel", "guild", channel.getId());
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

	public void setWelcomeChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".welcomechannel", "guild", channel.getId());
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

	public void setLogChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".textlog", "guild", channel.getId());
	}

	public TextChannel getVoiceLogChannel()
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

	public void setVoiceLogChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".voicelog", "guild", channel.getId());
	}

	public List<Role> getServerJoinRoles()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".serverjoinroles", "guild"))
		{
			return new ArrayList<>();
		}
		else
		{
			return Arrays.stream(Yaml.getFieldString(guildId + ".serverjoinroles", "guild").split(" ")).map(jda::getRoleById).filter(Objects::nonNull).collect(Collectors.toList());
		}
	}

	public void setServerJoinRoles(List<Role> roles)
	{
		StringBuilder roleString = new StringBuilder();
		roles.forEach(role -> roleString.append(role.getId()).append(" "));
		Yaml.updateField(guildId + ".serverjoinroles", "guild", roleString.toString());
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
	public void setVerifiedRole(Role role)
	{
		Yaml.updateField(guildId + ".verifiedrole", "guild", role.getId());
	}

	public String getGuildPrefix()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".prefix", "guild"))
		{
			return Constants.DEFAULT_BOT_PREFIX;
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
