package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.cache.PunishmentCache;
import org.igsq.igsqbot.entities.json.Report;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MessageReactionAdd_Report extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public MessageReactionAdd_Report(IGSQBot igsqBot) 
	{
		this.igsqBot = igsqBot;
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		if(!event.getReactionEmote().isEmoji() || !event.isFromGuild()) return;
		String messageID = event.getMessageId();
		Guild guild = event.getGuild();

		List<RestAction<?>> actions = new ArrayList<>();

		actions.add(event.retrieveMessage());
		actions.add(event.retrieveUser());
		actions.add(event.retrieveMember());

		RestAction.allOf(actions).queue(
				results ->
				{
					Message message = (Message) results.get(0);
					User user = (User) results.get(1);
					Member member = (Member) results.get(2);
					Report report = PunishmentCache.getInstance().getReport(messageID);
					if(report != null && !user.isBot())
					{
						Member reportedMember = guild.getMemberById(report.getReportedUser());
						MessageEmbed embed = message.getEmbeds().get(0);
						if(reportedMember == null)
						{
							message.editMessage(new EmbedBuilder(embed)
									.setFooter("This was dealt with by " + user.getAsTag() + ", the reported user left the server.")
									.setColor(Color.GREEN).build()).queue();
						}
						else if(event.getReactionEmote().isEmoji() && event.getReactionEmote().getEmoji().equalsIgnoreCase(Constants.THUMB_UP) && member.canInteract(reportedMember))
						{
							message.editMessage(new EmbedBuilder(embed)
									.setFooter("This was dealt with by " + user.getAsTag())
									.setColor(Color.GREEN).build()).queue();
						}
						else
						{
							event.getReaction().removeReaction(user).queue();
							return;
						}
						report.remove();
						message.clearReactions().queue();
					}
				});
	}
}
