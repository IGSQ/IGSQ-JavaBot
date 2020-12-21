package org.igsq.igsqbot.util;

import org.igsq.igsqbot.Yaml;

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
		while(!YamlUtils.isFieldEmpty(id + ".references." + i + ".name", "verification"))
		{
			if(!YamlUtils.isFieldEmpty(id + ".references." + i + ".aliases", "verification"))
			{
				String role = Yaml.getFieldString(id + ".references." + i + ".id", "verification");
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".aliases", "verification").split(","))
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
		while(!YamlUtils.isFieldEmpty(id + ".references." + i + ".name", "verification"))
		{
			if(!YamlUtils.isFieldEmpty(id + ".references." + i + ".aliases", "verification"))
			{
				String role = Yaml.getFieldString(id + ".references." + i + ".id", "verification");
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(","))
				{
					result.putIfAbsent(selectedAlias, role);
				}
			}
			i++;
		}
		return result;
	}

	public static void insertAlias(String id, String role, String alias)
	{
		int i = 0;
		while(!YamlUtils.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(id + ".references." + i + ".id", "verification").equals(role)))
			{
				if(YamlUtils.isFieldEmpty(id + ".references." + i + ".declined", "verification") || !ArrayUtils.isValueInArray(Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(","), alias))
				{
					if(YamlUtils.isFieldEmpty(id + ".references." + i + ".aliases", "verification"))
					{
						Yaml.updateField(id + ".references." + i + ".aliases", "verification", alias);
					}
					else
					{
						Yaml.updateField(id + ".references." + i + ".aliases", "verification", Yaml.getFieldString(id + ".references." + i + ".aliases", "verification") + "," + alias);
					}
					return;
				}
			}
			i++;
		}
		Yaml.updateField(id + ".references." + i + ".aliases", "verification", alias);
		Yaml.updateField(id + ".references." + i + ".id", "verification", role);
		Yaml.updateField(id + ".references." + i + ".name", "verification", alias);
	}

	public static void insertDecline(String id, String role, String alias)
	{
		int i = 0;
		while(!YamlUtils.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(id + ".references." + i + ".id", "verification").equals(role)))
			{
				if(YamlUtils.isFieldEmpty(id + ".references." + i + ".declined", "verification") || !ArrayUtils.isValueInArray(Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(","), alias))
				{
					if(YamlUtils.isFieldEmpty(id + ".references." + i + ".declined", "verification"))
					{
						Yaml.updateField(id + ".references." + i + ".declined", "verification", alias);
					}
					else
					{
						Yaml.updateField(id + ".references." + i + ".declined", "verification", Yaml.getFieldString(id + ".references." + i + ".aliases", "verification") + "," + alias);
					}
					return;
				}
			}
			i++;
		}
		Yaml.updateField(id + ".references." + i + ".declined", "verification", alias);
		Yaml.updateField(id + ".references." + i + ".id", "verification", role);
		Yaml.updateField(id + ".references." + i + ".name", "verification", alias);
	}

	public static boolean removeAlias(String guild, String role, String alias)
	{
		int i = 0;
		while(!YamlUtils.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(guild + ".references." + i + ".id", "verification").equals(role)))
			{
				String[] aliases = Yaml.getFieldString(guild + ".references." + i + ".aliases", "verification").split(",");
				StringBuilder dependedAliases = new StringBuilder();
				for(String selectedAlias : aliases)
				{
					if(!selectedAlias.equals(alias))
					{
						dependedAliases.append(",").append(selectedAlias);
					}
				}

				Yaml.updateField(guild + ".references." + i + ".aliases", "verification", dependedAliases.toString());
				return true;
			}
			i++;
		}
		return false;
	}

	public static boolean removeDecline(String guild, String role, String alias)
	{
		int i = 0;
		while(!YamlUtils.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(guild + ".references." + i + ".id", "verification").equals(role)))
			{
				String[] aliases = Yaml.getFieldString(guild + ".references." + i + ".declined", "verification").split(",");
				StringBuilder dependedAliases = new StringBuilder();
				for(String selectedAlias : aliases)
				{
					if(!selectedAlias.equals(alias))
					{
						dependedAliases.append(",").append(selectedAlias);
					}
				}

				Yaml.updateField(guild + ".references." + i + ".declined", "verification", dependedAliases.toString());
				return true;
			}
			i++;
		}
		return false;
	}

	public static boolean isArgsEmbedCompatible(List<String> args)
	{
		return Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList()).size() > EmbedUtils.CHARACTER_LIMIT;
	}
}
