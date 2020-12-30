package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessageReactionAdd_Report extends ListenerAdapter
{
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		if(!event.getReactionEmote().isEmoji()) return;
		final String messageID = event.getMessageId();
		final Guild guild = event.getGuild();
		final JDA jda = event.getJDA();

		List<RestAction<?>> actions = new ArrayList<>();

		actions.add(event.retrieveMessage());
		actions.add(event.retrieveUser());
		actions.add(event.retrieveMember());

		RestAction.allOf(actions).queue(
				results ->
				{
					Message message = (Message) results.get(0);
					User user= (User) results.get(1);
					Member member = (Member) results.get(2);
					final MessageDataCache messageDataCache = MessageDataCache.getMessageData(messageID, jda);
					if(messageDataCache != null && messageDataCache.getType().equals(MessageDataCache.MessageType.REPORT) && !user.isBot())
					{
						Member reportedMember = messageDataCache.getMembers(guild).get("reporteduser");
						MessageEmbed embed = message.getEmbeds().get(0);

						if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+2705") && member.canInteract(reportedMember))
						{
							EmbedUtils.sendReplacedEmbed(message, new EmbedBuilder(message.getEmbeds().get(0))
									.setFooter("This was dealt with by " + user.getAsTag())
									.setColor(Color.GREEN), true);
							YamlUtils.clearField(messageID + ".report", Filename.INTERNAL);
						}
						else
						{
							EmbedUtils.sendReplacedEmbed(message, new EmbedBuilder(message.getEmbeds().get(0))
									.setFooter(user.getAsTag() + ", you are not higher than " + reportedMember.getRoles().get(0).getName() + "."), 5000);
							event.getReaction().removeReaction(user).queue();
						}
					}
				});
	}
}
