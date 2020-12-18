package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.cache.MessageDataCache;
import org.igsq.igsqbot.util.YamlUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessageReactionAddEvent_Report extends ListenerAdapter
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

					if(Yaml.getFieldBool(messageID + ".report.enabled", "internal") && !user.isBot())
					{
						final MessageDataCache messageDataCache = MessageDataCache.getMessageData(messageID, jda);

						if(messageDataCache != null)
						{
							Member reportedMember = messageDataCache.getMembers(guild).get("reporteduser");
							MessageEmbed embed = message.getEmbeds().get(0);

							if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getAsCodepoints().equals("U+2705") && member.canInteract(reportedMember))
							{
								new EmbedGenerator(embed).footer("This was dealt with by " + user.getAsTag()).color(Color.GREEN).replace(message, true);
								YamlUtils.clearField(messageID + ".report", "internal");
							}
							else
							{
								new EmbedGenerator(embed)
										.color(Color.YELLOW)
										.footer(user.getAsTag() + ", you are not higher than " + reportedMember.getRoles().get(0).getName() + ".")
										.replace(message, 5000, embed);
								event.getReaction().removeReaction(user).queue();
							}
						}
					}
				});
	}
}
