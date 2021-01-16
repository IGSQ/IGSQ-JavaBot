package org.igsq.igsqbot.events.command;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Report;
import org.igsq.igsqbot.util.EmbedUtils;

public class ReportCommandReactionAdd extends ListenerAdapter
{
	private final IGSQBot igsqBot;

	public ReportCommandReactionAdd(IGSQBot igsqBot)
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
		actions.add(event.retrieveMember());

		RestAction.allOf(actions).queue(
				results ->
				{
					Message message = (Message) results.get(0);
					User user = (User) results.get(1);
					Member member = (Member) results.get(2);

					if(user.equals(igsqBot.getJDA().getSelfUser()))
					{
						return;
					}
					if(message.getEmbeds().isEmpty())
					{
						return;
					}

					Report report = Report.getById(message.getIdLong(), igsqBot);

					if(report != null)
					{
						Member reportedMember = guild.getMemberById(report.getReportedUserId());
						if(reportedMember == null)
						{
							EmbedUtils.sendReplacedEmbed(message, new EmbedBuilder(message.getEmbeds().get(0))
									.setColor(Color.GREEN)
									.setFooter("This report was dealt with by " + user.getAsTag() + " the reported member left."));
							report.remove();
							return;
						}

						if(!member.canInteract(reportedMember))
						{
							message.removeReaction(Constants.THUMB_UP, user).queue();
							return;
						}

						EmbedUtils.sendReplacedEmbed(message, new EmbedBuilder(message.getEmbeds().get(0))
								.setColor(Color.GREEN)
								.setFooter("This report was dealt with by " + user.getAsTag()));
						report.remove();
						message.clearReactions().queue();
					}
				});
	}
}
