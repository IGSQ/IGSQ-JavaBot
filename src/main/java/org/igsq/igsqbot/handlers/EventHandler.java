package org.igsq.igsqbot.handlers;

import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import org.igsq.igsqbot.events.command.MessageReactionAddEvent_Help;
import org.igsq.igsqbot.events.command.MessageReactionAddEvent_Report;
import org.igsq.igsqbot.events.logging.*;
import org.igsq.igsqbot.events.main.MessageDeleteEvent_Main;
import org.igsq.igsqbot.events.main.MessageReactionAddEvent_Main;
import org.igsq.igsqbot.events.main.MessageReactionRemoveEvent_Main;
import org.igsq.igsqbot.events.main.MessageReceivedEvent_Main;

public class EventHandler
{
	private static final InterfacedEventManager eventManager = new InterfacedEventManager();

	private EventHandler()
	{
		//Overrides the default, public, constructor
	}

	public static InterfacedEventManager getEventManager()
	{
		return eventManager;
	}

	public static void setEvents()
	{
		eventManager.register(new MessageReactionAddEvent_Main());
		eventManager.register(new MessageDeleteEvent_Main());
		eventManager.register(new MessageReceivedEvent_Main());
		eventManager.register(new MessageReactionRemoveEvent_Main());

		eventManager.register(new MessageReactionAddEvent_Help());
		eventManager.register(new MessageReactionAddEvent_Report());

		eventManager.register(new GuildMemberJoinEvent_Logging());
		eventManager.register(new GuildMemberRemoveEvent_Logging());

		eventManager.register(new GuildVoiceJoinEvent_Logging());
		eventManager.register(new GuildVoiceLeaveEvent_Logging());
		eventManager.register(new GuildVoiceMoveEvent_Logging());

		eventManager.register(new MessageBulkDeleteEvent_Logging());
		eventManager.register(new MessageDeleteEvent_Logging());
		eventManager.register(new MessageUpdateEvent_Logging());
	}
}
