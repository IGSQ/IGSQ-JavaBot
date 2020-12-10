package org.igsq.igsqbot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.Yaml;

public class Common {
	public static final String BOT_PREFIX = ".";
	public static final String[] THUMB_REACTIONS = {"U+1F44D","U+1F44E"};
	public static final String[] TICK_REACTIONS = {"U+2705","U+274E"};
	
	public static JDABuilder jdaBuilder;
	public static JDA jda;
	
	public static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static final ExecutorService commandExecutor = Executors.newFixedThreadPool(5);

	private Common()
	{
		//Overrides the default, public, constructor
	}
	
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
	public static Message[] depend(Message[] array, int location)
    {
		Message[] arrayDepended = new Message[array.length-1];
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
	
	public static boolean isFieldEmpty(String path, String filename)
	{
		return Yaml.getFieldString(path, filename) == null || Yaml.getFieldString(path, filename).isEmpty();
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
	
	public static Message[] depend(Message[] array, Message value)
    {
		Message[] arrayDepended = new Message[array.length-1];
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
		System.arraycopy(array, 0, arrayAppended, 0, array.length);
    	arrayAppended[array.length] = value;
    	return arrayAppended;
    }
    
    public static int[] append(int[] array, int value)
    {
    	int[] arrayAppended = new int[array.length+1];
		System.arraycopy(array, 0, arrayAppended, 0, array.length);
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
		System.arraycopy(array, 0, arrayAppended, 0, array.length);
    	arrayAppended[array.length] = value;
    	return arrayAppended;
    }
    
	public static String[][] append(String[][] array, String[] value) 
	{
    	String[][] arrayAppended = new String[array.length+1][];
		System.arraycopy(array, 0, arrayAppended, 0, array.length);
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
	
	public static boolean isUserMention(String arg)
	{
		try 
		{
			return arg.startsWith("<@!") && arg.endsWith(">")/* && arg.substring(3, 21).matches("[0-9]")*/;
		}
		catch(Exception exception)
		{
			return false;
		}
	}
	public static boolean isUserId(String arg) 
	{
		try 
		{
			return arg.substring(0, 18)/*.matches("[0-9]")*/ != null;
		}
		catch(Exception exception)
		{
			return false;
		}
	}
	public static String getUserIdFromMention(String arg)
	{
		if(isUserMention(arg)) return arg.substring(3, 21);
		else return null;
	}
	public static Member getMemberFromUser(User user,Guild guild) 
	{
		Member member = null;
		try 
		{
			member = guild.retrieveMember(user).complete();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return member;
	}
	public static User getUserFromMention(String arg)
	{
		if(isUserMention(arg)) return getUserById(arg.substring(3, 21));
		else if(isUserId(arg)) return getUserById(arg);
		else return null;
	}
	public static User getUserById(String id)
	{
		User user = null;
		try 
		{
			user = jda.retrieveUserById(id).complete();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return user;
	}
	
	public static Role getRoleFromMention(Guild guild, String arg)
	{
		if(isRoleMention(arg)) return guild.getRoleById(arg.substring(3, 21));
		else if(isRoleId(arg)) return guild.getRoleById(arg);
		else return null;
	}
	public static boolean isRoleId(String arg) 
	{
		try 
		{
			return arg.substring(0, 18)/*.matches("[0-9]")*/ != null;
		}
		catch(Exception exception)
		{
			return false;
		}
	}
	public static boolean isRoleMention(String arg)
	{
		try 
		{
			return arg.startsWith("<@&") && arg.endsWith(">")/* && arg.substring(3, 21).matches("[0-9]")*/;
		}
		catch(Exception exception)
		{
			return false;
		}
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
	
	public static String stringDepend(String input, String match)
	{
		if(!input.contains(match)) return input;
		input = input.substring(0, input.indexOf(match)) + input.substring(input.indexOf(match) + match.length());
		return input;
	}
	
	public static String getTimestamp()
	{
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now());
	}
	
	public static GuildChannel getLogChannel(String guildID)
	{
		return jda.getGuildChannelById(Yaml.getFieldString(guildID + ".textlog", "guild"));
	}
}
