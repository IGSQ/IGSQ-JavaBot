package org.igsq.igsqbot.logging;

public class Main_Logging
{
	public Main_Logging()
	{
		new MessageDeleteEvent_Logging();
		new MessageBulkDeleteEvent_Logging();
		new MessageUpdateEvent_Logging();
		new GuildMemberJoinEvent_Logging();
		new GuildMemberRemoveEvent_Logging();
	}
}
