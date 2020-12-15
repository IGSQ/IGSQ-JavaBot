package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.Message;
import org.igsq.igsqbot.objects.EmbedGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayUtils
{
	public static final List<EmbedGenerator> HELP_PAGE_TEXT = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(new EmbedGenerator().title("__**Help Page 1**__").element(".Avatar","Shows the avatar of mentioned user.\n.avatar [user]", true).element(".Poll", "Creates a poll using user input\n.poll [question]/[option1]/[option2]/etc", true).element(".Suggest", "Suggest an idea to the community\n.suggest [suggestion]", true),
													  new EmbedGenerator().title("__**Help Page 2**__").element("BIg noober","big noober very noober", true))));

	public static final List<EmbedGenerator> MODPAGE_TEXT = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(new EmbedGenerator().title("__**Mod Help Page 1**__").element(".Clear", "Clears messages by specified amount in the current channel.\n .clear [amount]", true).element(".Verify","Verifies specified user with aliases for this server.\n .verify [user]", true).element(".Alias","Adds alias' and declinations for this server, used in Verification.\n ADD SYNTAX HERE"))));

	private ArrayUtils()
	{
		//Overrides the default, public, constructor
	}
	public static String arrayCompile(String[] array, String delimiter)
	{
		StringBuilder builder = new StringBuilder();
		for(String selectedPart : array)
		{
			builder.append(selectedPart).append(delimiter);
		}
		return builder.toString().strip();
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
}
