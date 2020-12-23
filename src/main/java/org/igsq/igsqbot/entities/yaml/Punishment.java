package org.igsq.igsqbot.entities.yaml;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Punishment
{
	private final String memberId;
	private final String guildId;

	public Punishment(Member member)
	{
		this.memberId = member.getId();
		this.guildId = member.getGuild().getId();
	}

	public Punishment(String memberId, String guildId)
	{
		this.memberId =  memberId;
		this.guildId = guildId;
	}

	public void addWarning(String reason)
	{
		Yaml.updateField(
				guildId + "." + memberId + ".warnings",
				Filename.PUNISHMENT,

				YamlUtils.getFieldAppended(
						guildId + "." + memberId + ".warnings",
						Filename.PUNISHMENT,
						"\n",
						reason + " - " + StringUtils.getTimestamp()));
	}

	public String removeWarning(int number)
	{
		List<String> warnings = getWarnings();
		number--;
		if(number < 0 || number > warnings.size())
		{
			return null;
		}
		if(warnings.get(number) != null)
		{
			Yaml.updateField(guildId + "." + memberId + ".warnings",
					Filename.PUNISHMENT,
					ArrayUtils.arrayCompile(warnings, "\n"));
			return warnings.get(number);
		}
		else
		{
			return null;
		}
	}

	public List<String> getWarnings()
	{
		if(YamlUtils.isFieldEmpty(guildId + "." + memberId + ".warnings", Filename.PUNISHMENT))
		{
			return new ArrayList<>();
		}
		else
		{
			return new ArrayList<>(Arrays.asList(Yaml.getFieldString(guildId + "." + memberId + ".warnings", Filename.PUNISHMENT).split("\n")));
		}
	}

	public String compileWarnings()
	{
		StringBuilder compiledWarnings = new StringBuilder();
		int currentWarning = 1;
		for(String selectedWarning : getWarnings())
		{
			compiledWarnings.append(currentWarning).append(": ").append(selectedWarning).append("\n");
			currentWarning ++;
		}
		return compiledWarnings.toString();
	}

	public boolean addMute(String epochUntilUnmute)
	{
		Map<String, String> mutes = new ConcurrentHashMap<>();
		Arrays.stream(Yaml.getFieldString(guildId + ".mutes", Filename.PUNISHMENT).split(" "))
				.forEach(pair -> mutes.putIfAbsent(pair.split("/")[0], pair.split("/")[1]));

		if(mutes.get(memberId) == null)
		{
			StringBuilder muteBuilder = new StringBuilder();
			mutes.put(memberId, epochUntilUnmute);
			mutes.forEach((id, time) -> muteBuilder.append(id).append("/").append(time).append(" "));
			Yaml.updateField(guildId + ".mutes", Filename.PUNISHMENT, muteBuilder.toString());
			return true;
		}
		else
		{
			return false;
		}
	}

	public void removeMute()
	{
		Map<String, String> mutes = new ConcurrentHashMap<>();
		Arrays.stream(Yaml.getFieldString(guildId + ".mutes", Filename.PUNISHMENT).split(" "))
				.forEach(pair -> mutes.putIfAbsent(pair.split("/")[0], pair.split("/")[1]));

		if(mutes.get(memberId) != null)
		{
			StringBuilder muteBuilder = new StringBuilder();
			mutes.remove(memberId);
			mutes.forEach((id, time) -> muteBuilder.append(id).append("/").append(time).append(" "));
			Yaml.updateField(guildId + ".mutes", Filename.PUNISHMENT, muteBuilder.toString());
		}
	}

	public static void checkMutes(JDA jda)
	{
		jda.getGuilds().forEach(guild ->
		{
			if(!YamlUtils.isFieldEmpty(guild.getId() + ".mutes", Filename.PUNISHMENT))
			{
				Arrays.stream(Yaml.getFieldString(guild.getId() + ".mutes", Filename.PUNISHMENT).split(" "))
						.forEach(pair ->
						{
							if(Long.parseLong(pair.split("/")[1]) > System.currentTimeMillis())
							{
								new Punishment(pair.split("/")[0], pair.split("/")[1]).removeMute();
							}
						});
			}
		});
	}
}
