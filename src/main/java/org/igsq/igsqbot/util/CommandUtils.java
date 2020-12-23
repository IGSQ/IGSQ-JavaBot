package org.igsq.igsqbot.util;

import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.yaml.Filename;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".aliases", Filename.VERIFICATION).split(","))
				{
					result.putIfAbsent(selectedAlias, role);
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
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".declined", Filename.VERIFICATION).split(","))
				{
					result.putIfAbsent(selectedAlias, role);
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

	public static LocalTime parseTime(String arg)
	{
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
		LocalTime time;
		try
		{
			time = LocalTime.parse(arg, dateTimeFormatter);
		}
		catch(Exception exception)
		{
			return null;
		}
		return time;
	}
}
