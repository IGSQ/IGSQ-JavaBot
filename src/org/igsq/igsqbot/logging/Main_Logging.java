package org.igsq.igsqbot.logging;

public class Main_Logging
{
	public static MessageCache[] messageCaches = new MessageCache[0];
	
	public Main_Logging()
	{
		new MessageDeleteEvent_Logging();
		new MessageBulkDeleteEvent_Logging();
		new MessageUpdateEvent_Logging();
	}
}
