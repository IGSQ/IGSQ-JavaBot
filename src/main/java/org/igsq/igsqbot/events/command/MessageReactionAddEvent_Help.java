package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.cache.MessageDataCache;
import org.igsq.igsqbot.util.ArrayUtils;

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

		event.retrieveMessage().queue(
				message ->
						event.retrieveUser().queue(
								user ->
								{
									final MessageDataCache messageDataCache = MessageDataCache.getMessageData(messageID, jda);

									if(messageDataCache != null && messageDataCache.getType().equals(MessageDataCache.MessageType.HELP) && !user.isBot())
									{
										event.getReaction().removeReaction(user).queue();

										if(messageDataCache.getUsers().get(0).equals(user))
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
								})
		);
	}
}
