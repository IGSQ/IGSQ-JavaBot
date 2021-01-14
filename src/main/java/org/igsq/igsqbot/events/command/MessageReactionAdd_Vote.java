package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.database.Vote;

import java.util.ArrayList;
import java.util.List;

public class MessageReactionAdd_Vote extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageReactionAdd_Vote(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		if(!event.getReactionEmote().isEmoji())
		{
			return;
		}
		List<RestAction<?>> actions = new ArrayList<>();

		actions.add(event.retrieveMessage());
		actions.add(event.retrieveUser());

		RestAction.allOf(actions).queue(
				results ->
				{
					Message message = (Message) results.get(0);
					User author = (User) results.get(1);

					if(author.isBot())
					{
						return;
					}

					String unicode = event.getReactionEmote().getEmoji().substring(0,1);
					int maxVote = Vote.getMaxOption(message.getIdLong(), igsqBot);

					System.out.println(unicode);
				}
		);
	}
}
