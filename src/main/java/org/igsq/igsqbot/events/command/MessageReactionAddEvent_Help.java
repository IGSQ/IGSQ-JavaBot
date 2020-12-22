package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageReactionAddEvent_Help extends ListenerAdapter
{
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		if(!event.getReactionEmote().isEmoji()) return;
		final String messageID = event.getMessageId();
		final ReactionEmote reaction = event.getReactionEmote();
		final String codePoint = reaction.getAsCodepoints();
		final JDA jda = event.getJDA();

		List<RestAction<?>> actions = new ArrayList<>();

		actions.add(event.retrieveMessage());
		actions.add(event.retrieveUser());

		RestAction.allOf(actions).queue(
			results ->
			{
				final Message message = (Message) results.get(0);
				final User user = (User) results.get(1);

				final MessageDataCache messageDataCache = MessageDataCache.getMessageData(messageID, jda);

				if(messageDataCache != null && messageDataCache.getType().equals(MessageDataCache.MessageType.HELP) && !user.isBot())
				{
					event.getReaction().removeReaction(user).queue();

					if(messageDataCache.getUsers().get("user").equals(user))
					{
						int page = messageDataCache.getPage();

						if(reaction.isEmoji() && codePoint.equals("U+25c0"))
						{
							page--;
							if(page == 0) page = ArrayUtils.HELP_PAGE_TEXT.size();
							messageDataCache.setPage(page);
						}

						else if(reaction.isEmoji() && codePoint.equals("U+25b6"))
						{
							page++;
							if(page == ArrayUtils.HELP_PAGE_TEXT.size() + 1) page = 1;
							messageDataCache.setPage(page);
						}
						EmbedGenerator embed = ArrayUtils.HELP_PAGE_TEXT.get(page - 1);
						embed.setChannel(message.getChannel()).replace(message);
					}
				}
			}
		);
	}
}
