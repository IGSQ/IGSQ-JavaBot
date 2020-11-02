package org.igsq.igsqbot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.SelfUser;

public class Common {
	public static final String BOT_PREFIX = "-";
	public static final String[] STARTUP_MESSAGES = {"Hello and, again, welcome to the Aperture Science Computer-Aided Enrichment Center.",
			"Hello! This is the part where I kill you.",
			"I've been really busy being dead. You know, after you MURDERED ME.",
			"Am. Not. Dead! I'm not dead!",
			"I lie when I'm nervous."};
	public static final String[] COUNTRY_PREFIXES = {"united"};
	public static final String[] GAME_PREFIXES = {"rocket", "rainbow", "league", "csgo", "among"};
	public static final String[] GAMES = {"Rocket League", "Rainbow Six Siege", "Rainbow Six", "League Of Legends", "CS:GO", "Among Us"};
	
	public static JDABuilder jdaBuilder;
	public static JDA jda;
	public static SelfUser self;
	public final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static String[] depend(String[] array, int location)
    {
        String[] arrayDepended = new String[array.length-1];
        int hitRemove = 0;
        for (int i = 0;i < array.length;i++)
        {
            if(location != i){
                arrayDepended[i-hitRemove] = array[i];
            }
            else{
                hitRemove++;
            }
        }
        return arrayDepended;
    }
	public static String[] depend(String[] array, String value)
    {
        String[] arrayDepended = new String[array.length-1];
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
    public static String[] append(String[] array, String value)
    {
    	String[] arrayAppended = new String[array.length+1];
    	for (int i = 0;i < array.length;i++)
    	{
    		arrayAppended[i] = array[i];
    	}
    	arrayAppended[array.length] = value;
    	return arrayAppended;
    }
    public static String[] append(String[] array, String[] array2) 
    {
    	String[] appendedArray = array;
    	for (String string : array2) 
    	{
    		appendedArray = append(appendedArray,string);
    	}
    	return appendedArray;
    }
    public static Message[] append(Message[] array, Message value)
    {
    	Message[] arrayAppended = new Message[array.length+1];
    	for (int i = 0;i < array.length;i++)
    	{
    		arrayAppended[i] = array[i];
    	}
    	arrayAppended[array.length] = value;
    	return arrayAppended;
    }
    /**
     * Removes all text before a given character. If the character is not found the whole string is returned.
     * @return <b>String</b>
     */
    public static String removeBeforeCharacter(String string,char target) 
    {
    	Boolean targetFound = false;
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

	public static boolean isOption(String internal, String input,double accuracy)
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
				score += internal.length()-(i+2)/internal.length();
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
}
