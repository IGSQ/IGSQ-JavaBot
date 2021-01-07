package org.igsq.igsqbot.events.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Report;
import org.igsq.igsqbot.util.EmbedUtils;

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
		if(!event.getReactionEmote().isEmoji() || !event.isFromGuild())
		{
			return;
		}

		if(!event.getReactionEmote().getEmoji().equalsIgnoreCase(Constants.THUMB_UP))
		{
			return;
		}

		Guild guild = event.getGuild();

		MessageChannel reportChannel = guild.getTextChannelById(new GuildConfig(guild.getIdLong(), igsqBot).getReportChannel());

		if(reportChannel == null)
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
					User user = (User) results.get(1);

					if(!user.equals(igsqBot.getJDA().getSelfUser()))
					{
						return;
					}
					if(message.getEmbeds().isEmpty())
					{
						return;
					}

					Report report = Report.getById(user.getIdLong(), igsqBot);

					EmbedBuilder newEmbed = new EmbedBuilder(message.getEmbeds().get(0))
							.setColor(Color.GREEN)
							.setFooter("This report was dealt with by " + user.getAsTag());

					if(report != null)
					{
						EmbedUtils.sendReplacedEmbed(message, newEmbed);
						report.remove();
					}
				});
	}
}
