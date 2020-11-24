package org.igsq.igsqbot.logging;

public class Common_Logging 
{
	public static MessageCache[] append(MessageCache[] array, MessageCache value)
	{
		MessageCache[] arrayAppended = new MessageCache[array.length+1];
		for (int i = 0;i < array.length;i++)
		{
			arrayAppended[i] = array[i];
		}
		arrayAppended[array.length] = value;
		return arrayAppended;
	}
	
	public static void cleanCaches()
	{
		for(MessageCache selectedCache : Main_Logging.messageCaches)
		{
			selectedCache.clean();
		}
	}
	
	public static MessageCache retrieveCache(String ID)
	{
		for(MessageCache selectedCache : Main_Logging.messageCaches)
		{
			if(selectedCache.getID().equals(ID))
			{
				return selectedCache;
			}
		}
		return null;
	}
	
	public static boolean isCache(String ID)
	{
		for(MessageCache selectedCache : Main_Logging.messageCaches)
		{
			if(selectedCache.getID().equals(ID))
			{
				return true;
			}
		}
		return false;
	}
}
