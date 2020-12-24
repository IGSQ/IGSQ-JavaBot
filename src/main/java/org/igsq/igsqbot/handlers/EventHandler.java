package org.igsq.igsqbot.handlers;

import net.dv8tion.jda.api.hooks.InterfacedEventManager;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Help;
import org.igsq.igsqbot.events.command.MessageReactionAdd_Report;
import org.igsq.igsqbot.events.logging.*;
import org.igsq.igsqbot.events.main.*;

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

	public static void registerEvents()
	{
		eventManager.register(new MessageReactionAdd_Main());
		eventManager.register(new MessageDelete_Main());
		eventManager.register(new MessageReceived_Main());
		eventManager.register(new MessageReactionRemove_Main());

		eventManager.register(new GuildLeave_Main());
		eventManager.register(new UnavailableGuildLeave_Main());

		eventManager.register(new MessageReactionAdd_Help());
		eventManager.register(new MessageReactionAdd_Report());

		eventManager.register(new GuildMemberJoin_Logging());
		eventManager.register(new GuildMemberRemove_Logging());

		eventManager.register(new GuildVoiceJoin_Logging());
		eventManager.register(new GuildVoiceLeave_Logging());
		eventManager.register(new GuildVoiceMove_Logging());

		eventManager.register(new MessageBulkDelete_Logging());
		eventManager.register(new MessageDelete_Logging());
		eventManager.register(new MessageUpdate_Logging());
	}
}
