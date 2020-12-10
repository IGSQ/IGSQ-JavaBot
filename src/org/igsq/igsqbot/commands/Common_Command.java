package org.igsq.igsqbot.commands;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.Yaml;

public class Common_Command 
{
	private Common_Command()
	{ } // Override the default constructor
	public static final String[] POLL_EMOJIS_UNICODE = {"U+1F350", " U+1F349", "U+1F34D", "U+1F34E", "U+1F34C", "U+1F951", "U+1F346", "U+1F95D", "U+1F347", "U+1FAD0", "U+1F352", "U+1F9C5", "U+1F351", "U+1F34B", "U+1F34A","U+1F348", "U+1F965", "U+1F9C4", "U+1F952", "U+1F991"};
	public static final String[] POLL_EMOJIS = {":pear:", ":watermelon:", ":pineapple:", ":apple:", ":banana:", ":avocado:", ":eggplant:", ":kiwi:", ":grapes:", ":blueberries:", ":cherries:", ":onion:", ":peach:", ":lemon:", ":tangerine:", ":melon:", ":coconut:",":garlic:", ":cucumber:", ":squid:"};
	public static final EmbedGenerator[] HELP_PAGE_TEXT = {new EmbedGenerator(null).title("__**Help Page 1**__").element(".Avatar","Shows the avatar of mentioned user.\n.avatar [user]", true).element(".Poll", "Creates a poll using user input\n.poll [question]/[option1]/[option2]/etc", true).element(".Suggest", "Suggest an idea to the community\n.suggest [suggestion]", true),
													  new EmbedGenerator(null).title("__**Help Page 2**__").element("BIg noober","big noober very noober", true)};
	public static final EmbedGenerator[] MODPAGE_TEXT = {new EmbedGenerator(null).title("__**Mod Help Page 1**__").element(".Clear", "Clears messages by specified amount in the current channel.\n .clear [amount]", true).element(".Verify","Verifies specified user with aliases for this server.\n .verify [user]", true).element(".Alias","Adds alias' and declinations for this server, used in Verification.\n ADD SYNTAX HERE")};
	public static final String[] SHUTDOWN_MESSAGES = {"Goodbye, Caroline.",
			"Federal Superfund regulations require us to inform you that you must now leave the theater, as measuring the effects of asbestos-lined promotional clothing is not part of today's presentation. Enjoy your free t-shirt. Goodbye.",
			"You know what, this plan is so good, I'm going to give you a sporting chance and turn off the neurotoxin. I'm joking. Of course. Goodbye.",
			"Shutting down",
			"Good news! I can use this equipment to shut down the neurotoxin system. It is, however, password protected. AH! Alarm bells! No. Don't worry. Not a problem for me.",
			"All reactor core safeguards are now non-functional. Please prepare for reactor core meltdown.",
			"Undelete, undelete! Where's the undelete button?",
			"Now then, let's see what we got here. Ah! 'Reactor Core Emergency Heat Venting Protocols.' That's the problem right there, isn't it? 'Emergency'. You don't want to see 'emergency' flashing at you. Never good that, is it? Right. DELETE.",
			"In order to ensure that sufficient power remains for core testing protocols, all safety devices have been disabled.",
			"Okay, listen, we should get our stories straight, alright? If anyone asks -- and no one's gonna ask, don't worry -- but if anyone asks, tell them as far as you know, the last time you checked, everyone looked pretty much alive. Alright? Not dead.",
			"Well. I suppose we could just sit in this room and glare at each other until somebody drops dead, but I have a better idea."};
    
	
	public static CooldownHandler[] append(CooldownHandler[] array, CooldownHandler value)
	{
		CooldownHandler[] arrayAppended = new CooldownHandler[array.length+1];
        System.arraycopy(array, 0, arrayAppended, 0, array.length);
		arrayAppended[array.length] = value;
		return arrayAppended;
	}

	public static CooldownHandler[] depend(CooldownHandler[] array, CooldownHandler value)
	{
		CooldownHandler[] arrayDepended = new CooldownHandler[array.length-1];
	    int hitRemove = 0;
	    
	    for (int i = 0;i < array.length;i++)
	    {
	        if(!value.equals(array[i]))
	        {
	            arrayDepended[i-hitRemove] = array[i];
	        }
	        else
	        {
	            hitRemove++;
	        }
	    }
	    return arrayDepended;
	}
	public static String[][] getAliases(String id)
	{
		int i = 0;
		String[][] result = new String[0][0];
		while(!Common.isFieldEmpty(id + ".references." + i + ".name", "verification"))
		{
			if(!Common.isFieldEmpty(id + ".references." + i + ".aliases", "verification"))
			{
				String[] role = new String[0];
				role = Common.append(role, Yaml.getFieldString(id + ".references." + i + ".id", "verification"));
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".aliases", "verification").split(",")) role = Common.append(role, selectedAlias);
				result = Common.append(result, role);
			}
			else
			{
				result = Common.append(result, new String[0]);
			}
			i++;
		}
		return result;
	}
	public static void insertAlias(String id, String role, String alias)
	{
		int i = 0;
		while(!Common.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(id + ".references." + i + ".id", "verification").equals(role)))
			{
				if(Common.isFieldEmpty(id + ".references." + i + ".declined", "verification") || !Common.isValueInArray(Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(","), alias))
				{
					if(Common.isFieldEmpty(id + ".references." + i + ".aliases", "verification"))
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
		while(!Common.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
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
		while(!Common.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
		{
			i++;
		}
		return i;
	}
	public static void insertDecline(String id, String role, String alias)
	{
		int i = 0;
		while(!Common.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if((Yaml.getFieldString(id + ".references." + i + ".id", "verification").equals(role)))
			{
				if(Common.isFieldEmpty(id + ".references." + i + ".declined", "verification") || !Common.isValueInArray(Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(","), alias))
				{
					if(Common.isFieldEmpty(id + ".references." + i + ".declined", "verification"))
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
		while(!Common.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
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
		while(!Common.isFieldEmpty(guild + ".references." + i + ".id", "verification"))
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
		while(!Common.isFieldEmpty(id + ".references." + i + ".name", "verification"))
		{
			result = Common.append(result, Yaml.getFieldString(id + ".references." + i + ".id", "verification"));
			i++;
		}
		return result;
	}
	public static boolean isAliasExists(String guild, String alias)
	{
		int i = 0;
		while(!Common.isFieldEmpty(guild + ".references." + i + ".name", "verification"))
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
		while(!Common.isFieldEmpty(guild + ".references." + i + ".name", "verification"))
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
		while(!Common.isFieldEmpty(id + ".references." + i + ".id", "verification"))
		{
			if(!Common.isFieldEmpty(id + ".references." + i + ".declined", "verification"))
			{
				String[] role = new String[0];
				role = Common.append(role, Yaml.getFieldString(id + ".references." + i + ".id", "verification"));
				for(String selectedAlias : Yaml.getFieldString(id + ".references." + i + ".declined", "verification").split(",")) role = Common.append(role, selectedAlias);
				result = Common.append(result, role);
			}
			else
			{
				result = Common.append(result, new String[0]);
			}
			i++;
		}
		return result;
	}
}
