package org.igsq.igsqbot.util;

import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Command_Utils
{
	private Command_Utils()
	{
		// Override the default, public, constructor
	}
	public static final List<String> POLL_EMOJIS_UNICODE = Collections.unmodifiableList(new ArrayList<>(Arrays.asList("U+1F350", " U+1F349", "U+1F34D", "U+1F34E", "U+1F34C", "U+1F951", "U+1F346", "U+1F95D", "U+1F347", "U+1FAD0", "U+1F352", "U+1F9C5", "U+1F351", "U+1F34B", "U+1F34A","U+1F348", "U+1F965", "U+1F9C4", "U+1F952", "U+1F991")));
	public static final List<String> POLL_EMOJIS = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(":pear:", ":watermelon:", ":pineapple:", ":apple:", ":banana:", ":avocado:", ":eggplant:", ":kiwi:", ":grapes:", ":blueberries:", ":cherries:", ":onion:", ":peach:", ":lemon:", ":tangerine:", ":melon:", ":coconut:",":garlic:", ":cucumber:", ":squid:")));
	public static final List<EmbedGenerator> HELP_PAGE_TEXT = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(new EmbedGenerator().title("__**Help Page 1**__").element(".Avatar","Shows the avatar of mentioned user.\n.avatar [user]", true).element(".Poll", "Creates a poll using user input\n.poll [question]/[option1]/[option2]/etc", true).element(".Suggest", "Suggest an idea to the community\n.suggest [suggestion]", true),
													  new EmbedGenerator().title("__**Help Page 2**__").element("BIg noober","big noober very noober", true))));
	public static final List<EmbedGenerator> MODPAGE_TEXT = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(new EmbedGenerator().title("__**Mod Help Page 1**__").element(".Clear", "Clears messages by specified amount in the current channel.\n .clear [amount]", true).element(".Verify","Verifies specified user with aliases for this server.\n .verify [user]", true).element(".Alias","Adds alias' and declinations for this server, used in Verification.\n ADD SYNTAX HERE"))));

	public static String[][] getAliases(String id)
	{
		int i = 0;
		String[][] result = new String[0][0];
		while(!Yaml_Utils.isFieldEmpty(id + ".references." + i + ".name", "verification"))
		{
			if(!Yaml_Utils.isFieldEmpty(id + ".references." + i + ".aliases", "verification"))
			{
				String[] role = new String[0];
				role = Array_Utils.append(role, Yaml.getFieldString(id + ".references." + i + ".id", "verification"));
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".aliases", "verification").split(",")) role = Array_Utils.append(role, selectedAlias);
				result = Array_Utils.append(result, role);
			}
			else
			{
				result = Array_Utils.append(result, new String[0]);
			}
			i++;
		}
		return result;
	}
	public static void insertAlias(String id, String role, String alias)
	{
		int i = 0;
		while(!Yaml_Utils.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(id + ".references." + i + ".id", "verification").equals(role)))
			{
				if(Yaml_Utils.isFieldEmpty(id + ".references." + i + ".declined", "verification") || !Array_Utils.isValueInArray(Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(","), alias))
				{
					if(Yaml_Utils.isFieldEmpty(id + ".references." + i + ".aliases", "verification"))
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
	public static int findReferenceForRole(String guild, String role)
	{
		int i = 0;
		while(!Yaml_Utils.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
		{
			if(Yaml.getFieldString(guild + ".references." + i + ".id", "verification").equals(role))
			{
				return i;
			}
			i++;
		}
		return -1;
	}
	public static int findHighestReference(String guild)
	{
		int i = -1;
		while(!Yaml_Utils.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
		{
			i++;
		}
		return i;
	}
	public static void insertDecline(String id, String role, String alias)
	{
		int i = 0;
		while(!Yaml_Utils.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(id + ".references." + i + ".id", "verification").equals(role)))
			{
				if(Yaml_Utils.isFieldEmpty(id + ".references." + i + ".declined", "verification") || !Array_Utils.isValueInArray(Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(","), alias))
				{
					if(Yaml_Utils.isFieldEmpty(id + ".references." + i + ".declined", "verification"))
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
		while(!Yaml_Utils.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
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
		while(!Yaml_Utils.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
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
	public static String[] getRoles(String id)
	{
		int i = 0;
		String[] result = new String[0];
		while(!Yaml_Utils.isFieldEmpty(id + ".references." + i + ".name", "verification"))
		{
			result = Array_Utils.append(result, Yaml.getFieldString(id + ".references." + i + ".id", "verification"));
			i++;
		}
		return result;
	}
	public static boolean isAliasExists(String guild, String alias)
	{
		int i = 0;
		while(!Yaml_Utils.isFieldEmpty(guild + ".references." + i + ".name", "verification"))
		{
			for(String selectedAlias : Yaml.getFieldString(guild + ".references." + i + ".aliases", "verification").split(","))
			{
				if(selectedAlias.equals(alias))
				{
					return true;
				}
			}
			i++;
		}
		return false;
	}
	public static boolean isDeclinedExist(String guild, String alias)
	{
		int i = 0;
		while(!Yaml_Utils.isFieldEmpty(guild + ".references." + i + ".name", "verification"))
		{
			for(String selectedAlias : Yaml.getFieldString(guild + ".references." + i + ".declined", "verification").split(","))
			{
				if(selectedAlias.equals(alias))
				{
					return true;
				}
			}
			i++;
		}
		return false;
	}
	public static String[][] getDeclined(String id)
	{
		int i = 0;
		String[][] result = new String[0][0];
		while(!Yaml_Utils.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if(!Yaml_Utils.isFieldEmpty(id + ".references." + i + ".declined", "verification"))
			{
				String[] role = new String[0];
				role = Array_Utils.append(role, Yaml.getFieldString(id + ".references." + i + ".id", "verification"));
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(",")) role = Array_Utils.append(role, selectedAlias);
				result = Array_Utils.append(result, role);
			}
			else
			{
				result = Array_Utils.append(result, new String[0]);
			}
			i++;
		}
		return result;
	}
}
