package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.User;
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
		if(!event.getReactionEmote().isEmoji()) return;
		String messageID = event.getMessageId();
		ReactionEmote reaction = event.getReactionEmote();
		String codePoint = reaction.getAsCodepoints();
		JDA jda = event.getJDA();

		List<RestAction<?>> actions = new ArrayList<>();

		actions.add(event.retrieveMessage());
		actions.add(event.retrieveUser());

		RestAction.allOf(actions).queue(
				results ->
				{
					Message message = (Message) results.get(0);
					User user = (User) results.get(1);

					if(!user.isBot())
					{
						List<EmbedBuilder> PAGES = igsqBot.getHelpPages();

						int page = 10000; //TO BE IMPLEMENTED

						if(reaction.isEmoji() && codePoint.equals("U+25c0"))
						{
							page--;
							if(page == 0) page = PAGES.size();
						}

						else if(reaction.isEmoji() && codePoint.equals("U+25b6"))
						{
							page++;
							if(page == PAGES.size() + 1) page = 1;
						}
						else if(reaction.isEmoji() && codePoint.equals("U+274c"))
						{
							message.delete().queue();
							return;
						}
						if(event.isFromGuild() && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
						{
							event.getReaction().removeReaction(user).queue();
						}

						message.editMessage(PAGES.get(page - 1).build()).queue();
					}
				}
		);
	}
}
