package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.Message;

public class Array_Utils
{
	private Array_Utils()
	{
		//Overrides the default, public, constructor
	}
	public static String arrayCompile(String[] array)
	{
		StringBuilder builder = new StringBuilder();
		for(String selectedPart : array)
		{
			builder.append(selectedPart);
		}
		return builder.toString();
	}
	public static String arrayCompile(String[] array, boolean addSpaces)
	{
		StringBuilder builder = new StringBuilder();
		if(addSpaces)
		{
			for(String selectedPart : array)
			{
				builder.append(selectedPart).append(" ");
			}
		}
		else
		{
			for(String selectedPart : array)
			{
				builder.append(selectedPart);
			}
		}
		return builder.toString();
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
