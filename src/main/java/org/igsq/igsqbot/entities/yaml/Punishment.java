package org.igsq.igsqbot.entities.yaml;

import net.dv8tion.jda.api.entities.Member;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Punishment
{
	private final Member member;

	public Punishment(Member member)
	{
		this.member = member;
	}

	public void addWarning(String reason)
	{
		Yaml.updateField(
				member.getGuild().getId() + "." + member.getId() + ".warnings",
				Filename.PUNISHMENT,

				YamlUtils.getFieldAppended(
						member.getGuild().getId() + "." + member.getId() + ".warnings",
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
			Yaml.updateField(member.getGuild().getId() + "." + member.getId() + ".warnings",
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
		if(YamlUtils.isFieldEmpty(member.getGuild().getId() + "." + member.getId() + ".warnings", Filename.PUNISHMENT))
		{
			return new ArrayList<>();
		}
		else
		{
			return new ArrayList<>(Arrays.asList(Yaml.getFieldString(member.getGuild().getId() + "." + member.getId() + ".warnings", Filename.PUNISHMENT).split("\n")));
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
}
