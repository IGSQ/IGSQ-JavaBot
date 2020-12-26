package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.JDA;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.entities.yaml.GuildConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandUtils
{
	public static final List<String> POLL_EMOJIS_UNICODE = List.of("U+1F350", " U+1F349", "U+1F34D", "U+1F34E", "U+1F34C", "U+1F951", "U+1F346", "U+1F95D", "U+1F347", "U+1FAD0", "U+1F352", "U+1F9C5", "U+1F351", "U+1F34B", "U+1F34A", "U+1F348", "U+1F965", "U+1F9C4", "U+1F952", "U+1F991");
	public static final List<String> POLL_EMOJIS = List.of(":pear:", ":watermelon:", ":pineapple:", ":apple:", ":banana:", ":avocado:", ":eggplant:", ":kiwi:", ":grapes:", ":blueberries:", ":cherries:", ":onion:", ":peach:", ":lemon:", ":tangerine:", ":melon:", ":coconut:", ":garlic:", ":cucumber:", ":squid:");
	private CommandUtils()
	{
		// Override the default, public, constructor
	}

	public static Map<String, String> getAliases(String id)
	{
		int i = 0;
		Map<String, String> result = new HashMap<>();
		while(!YamlUtils.isFieldEmpty(id + ".references." + i + ".name", Filename.VERIFICATION))
		{
			if(!YamlUtils.isFieldEmpty(id + ".references." + i + ".aliases", Filename.VERIFICATION))
			{
				String role = Yaml.getFieldString(id + ".references." + i + ".id", Filename.VERIFICATION);
				if(!YamlUtils.isFieldEmpty(id + ".references." + i + ".aliases", Filename.VERIFICATION))
				{
					for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".aliases", Filename.VERIFICATION).split(","))
					{
						result.putIfAbsent(selectedAlias, role);
					}
				}
			}
			i++;
		}
		return result;
	}

	public static Map<String, String> getDeclined(String id)
	{
		int i = 0;
		Map<String, String> result = new HashMap<>();
		while(!YamlUtils.isFieldEmpty(id + ".references." + i + ".name", Filename.VERIFICATION))
		{
			if(!YamlUtils.isFieldEmpty(id + ".references." + i + ".aliases", Filename.VERIFICATION))
			{
				String role = Yaml.getFieldString(id + ".references." + i + ".id", Filename.VERIFICATION);
				if(!YamlUtils.isFieldEmpty(id + ".references." + i + ".declined", Filename.VERIFICATION))
				{
					for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".declined", Filename.VERIFICATION).split(","))
					{
						result.putIfAbsent(selectedAlias, role);
					}
				}
			}
			i++;
		}
		return result;
	}

	public static boolean isArgsEmbedCompatible(List<String> args)
	{
		return Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList()).size() > EmbedUtils.CHARACTER_LIMIT;
	}

	public static LocalDateTime parseTime(String arg)
	{
		if(arg.length() > 3 || arg.isEmpty() || !arg.matches("([0-9][mhdMHD]|[0-9][0-9][mhdMHD])"))
		{
			return null;
		}

		String unit = arg.substring(arg.length() - 1);
		String quantity = arg.substring(0, arg.length() - 1);
		LocalDateTime result = LocalDateTime.now();

		switch(unit.toLowerCase())
		{
			case "m":
				result = result.plusMinutes(Long.parseLong(quantity));
				break;
			case "h":
				result = result.plusHours(Long.parseLong(quantity));
				break;
			case "d":
				result = result.plusDays(Long.parseLong(quantity));
				break;
		}
		return result;
	}

	public static boolean isValidCommand(String message, String guildId, JDA jda)
	{
		return message.startsWith(new GuildConfig(guildId, jda).getGuildPrefix()) || message.startsWith("<@" + IGSQBot.SelfUser.getId() + ">") || message.startsWith("<@!" + IGSQBot.SelfUser.getId() + ">");
	}
}
