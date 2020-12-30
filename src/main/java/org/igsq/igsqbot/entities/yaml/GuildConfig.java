package org.igsq.igsqbot.entities.yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.json.Filename;
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
		if(YamlUtils.isFieldEmpty(guildId + ".verificationchannel", Filename.GUILD))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".verificationchannel", Filename.GUILD));
		}
	}

	public void setVerificationChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".verificationchannel", Filename.GUILD, channel.getId());
	}

	public TextChannel getReportChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".reportchannel", Filename.GUILD))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".reportchannel", Filename.GUILD));
		}
	}

	public void setReportChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".reportchannel", Filename.GUILD, channel.getId());
	}

	public TextChannel getWelcomeChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".welcomechannel", Filename.GUILD))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".welcomechannel", Filename.GUILD));
		}
	}

	public void setWelcomeChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".welcomechannel", Filename.GUILD, channel.getId());
	}

	public TextChannel getLogChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".textlog", Filename.GUILD))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".textlog", Filename.GUILD));
		}
	}

	public void setLogChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".textlog", Filename.GUILD, channel.getId());
	}

	public TextChannel getVoiceLogChannel()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".voicelog", Filename.GUILD))
		{
			return null;
		}
		else
		{
			return jda.getTextChannelById(Yaml.getFieldString(guildId + ".voicelog", Filename.GUILD));
		}
	}

	public void setVoiceLogChannel(TextChannel channel)
	{
		Yaml.updateField(guildId + ".voicelog", Filename.GUILD, channel.getId());
	}

	public List<Role> getServerJoinRoles()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".serverjoinroles", Filename.GUILD))
		{
			return new ArrayList<>();
		}
		else
		{
			return Arrays.stream(Yaml.getFieldString(guildId + ".serverjoinroles", Filename.GUILD).split(" ")).map(jda::getRoleById).filter(Objects::nonNull).collect(Collectors.toList());
		}
	}

	public void setServerJoinRoles(List<Role> roles)
	{
		StringBuilder roleString = new StringBuilder();
		roles.forEach(role -> roleString.append(role.getId()).append(" "));
		Yaml.updateField(guildId + ".serverjoinroles", Filename.GUILD, roleString.toString());
	}

	public Role getVerifiedRole()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".verifiedrole", Filename.GUILD))
		{
			return null;
		}
		else
		{
			return jda.getRoleById(Yaml.getFieldString(guildId + ".verifiedrole", Filename.GUILD));
		}

	}
	public void setVerifiedRole(Role role)
	{
		Yaml.updateField(guildId + ".verifiedrole", Filename.GUILD, role.getId());
	}

	public String getGuildPrefix()
	{
		if(YamlUtils.isFieldEmpty(guildId + ".prefix", Filename.GUILD))
		{
			return Constants.DEFAULT_BOT_PREFIX;
		}
		else
		{
			return Yaml.getFieldString(guildId + ".prefix", Filename.GUILD).trim();
		}
	}

	public void wipe()
	{
		YamlUtils.clearField(guildId, Filename.GUILD);
	}

	public void setGuildPrefix(String prefix)
	{
		Yaml.updateField(guildId + ".prefix", Filename.GUILD, " " + prefix);
	}
}
