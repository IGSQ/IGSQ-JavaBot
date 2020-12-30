package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.commands.HelpCommand;
import org.igsq.igsqbot.entities.cache.MessageDataCache;

import java.util.ArrayList;
import java.util.List;

public class MessageReactionAdd_Help extends ListenerAdapter
{
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

					MessageDataCache messageDataCache = MessageDataCache.getMessageData(messageID, jda);

					if(messageDataCache != null && messageDataCache.getType().equals(MessageDataCache.MessageType.HELP) && !user.isBot())
					{

						List<EmbedBuilder> PAGES = HelpCommand.getPages();

						if(messageDataCache.getUsers().get("user").equals(user))
						{
							int page = messageDataCache.getPage();

							if(reaction.isEmoji() && codePoint.equals("U+25c0"))
							{
								page--;
								if(page == 0) page = PAGES.size();
								messageDataCache.setPage(page);
							}

							else if(reaction.isEmoji() && codePoint.equals("U+25b6"))
							{
								page++;
								if(page == PAGES.size() + 1) page = 1;
								messageDataCache.setPage(page);
							}
							else if(reaction.isEmoji() && codePoint.equals("U+274c"))
							{
								messageDataCache.remove();
								message.delete().queue();
								return;
							}
							event.getReaction().removeReaction(user).queue();
							message.editMessage(PAGES.get(page - 1).build()).queue();
						}
					}
				}
		);
	}
}
