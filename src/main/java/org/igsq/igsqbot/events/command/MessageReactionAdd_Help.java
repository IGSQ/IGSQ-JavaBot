package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.IGSQBot;

import java.util.ArrayList;
import java.util.List;

public class MessageReactionAdd_Help extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageReactionAdd_Help(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		List<RestAction<?>> actions = new ArrayList<>();

		actions.add(event.retrieveMessage());
		actions.add(event.retrieveUser());

		RestAction.allOf(actions).queue(
				results ->
				{
				//TODO: this
				}
		);
	}
}
