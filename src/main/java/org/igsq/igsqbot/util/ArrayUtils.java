package org.igsq.igsqbot.util;

import org.igsq.igsqbot.objects.EmbedGenerator;

import java.util.List;

public class ArrayUtils
{
	public static final List<EmbedGenerator> HELP_PAGE_TEXT = List.of(new EmbedGenerator().title("__**Help Page 1**__").element(".Avatar", "Shows the avatar of mentioned user.\n.avatar [user]", true).element(".Poll", "Creates a poll using user input\n.poll [question]/[option1]/[option2]/etc", true).element(".Suggest", "Suggest an idea to the community\n.suggest [suggestion]", true), new EmbedGenerator().title("__**Help Page 2**__").element("BIg noober", "big noober very noober", true));

	public static final List<EmbedGenerator> MODPAGE_TEXT = List.of(new EmbedGenerator().title("__**Mod Help Page 1**__").element(".Clear", "Clears messages by specified amount in the current channel.\n .clear [amount]", true).element(".Verify", "Verifies specified user with aliases for this server.\n .verify [user]", true).element(".Alias", "Adds alias' and declinations for this server, used in Verification.\n ADD SYNTAX HERE"));

	private ArrayUtils()
	{
		//Overrides the default, public, constructor
	}

	public static String arrayCompile(List<String> array, String delimiter)
	{
		StringBuilder builder = new StringBuilder();
		for(String selectedPart : array)
		{
			builder.append(selectedPart).append(delimiter);
		}
		return builder.toString().strip();
	}

	public static boolean isValueInArray(Object[] array, Object value)
	{
		if(array.length == 0) return false;
		for(Object currentObject : array)
		{
			if(currentObject.equals(value))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isValueInArray(List<Object> array, Object value)
	{
		if(array.isEmpty()) return false;
		for(Object currentObject : array)
		{
			if(currentObject.equals(value))
			{
				return true;
			}
		}
		return false;
	}
}
