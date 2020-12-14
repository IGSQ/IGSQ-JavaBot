package org.igsq.igsqbot.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtils
{
	private StringUtils()
	{
		//Overrides the default, public, constructor
	}
	/**
     * Removes all text before a given character. If the character is not found the whole string is returned.
     * @return <b>String</b>
     */
    public static String removeBeforeCharacter(String string,char target)
    {
    	boolean targetFound = false;
    	char[] charArray = string.toCharArray();
    	String rebuiltString = "";
    	for(int i = 0;i < string.length();i++)
    	{
    		if(!targetFound)
    		{
    			if(charArray[i] == target) targetFound = true;
    		}
    		else rebuiltString += charArray[i];
    	}
    	if(targetFound) return rebuiltString;
    	else return string;
    }

	public static String getChannelAsMention(String channelID)
	{
		return "<#" + channelID + ">";
	}

	public static boolean isOption(String internal, String input, double accuracy)
	{
		if(internal == null || input == null || internal.length() == 0 || input.length() == 0) return false;
		internal = internal.toUpperCase().replaceAll("[^A-Z]", "");
		input = input.toUpperCase().replaceAll("[^A-Z]", "");
		if(input.equals(internal)) return true; //Perfect Outcome
		if(!input.startsWith(internal.split("")[0])) return false; //First character does not match it is most likely going to be a false positive so ignore it
		if (Math.abs(input.length() - internal.length()) > 3) return false; // Word Length Difference is too big.
		double score = 0;
		char[] internalChar = internal.toCharArray();
		int previousError = Integer.MIN_VALUE;
		char[] inputChar = input.toCharArray();
		for (int i = 1;i < internal.length();i++)
		{
			int charScore = Integer.MAX_VALUE;
			boolean found = false;
			for(int j = 0; j < input.length();j++)
			{
				if(internalChar[i] == inputChar[j] && charScore > Math.abs(j-i))
				{
					charScore = j-i;
					found = true;
				}
			}
			if(!found)
			{
				score +=  internal.length() - (double) (i + 2) / internal.length();
				previousError = 1;
			}
			else if(previousError != charScore)
			{
				previousError = charScore;
				score += Math.abs((double)charScore)/internal.length();
			}
		}
		return score < accuracy;
	}

	public static String stringDepend(String input, String match, String delimiter)
	{
		if(!input.contains(match)) return input;
		return input.substring(input.startsWith(delimiter) ? 1 : 0, input.indexOf(match)) + input.substring(input.indexOf(match) + match.length());
	}

	public static String getTimestamp()
	{
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now());
	}
}
