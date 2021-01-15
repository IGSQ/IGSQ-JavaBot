package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.time.LocalDateTime;
import java.util.List;

public class Vote
{
	private final List<Long> users;
	private final List<String> options;
	private final IGSQBot igsqBot;
	private final LocalDateTime timestamp;
	private final String subject;
	private final MessageChannel voteChannel;

	public Vote(List<String> options, List<Long> users, LocalDateTime timestamp, String subject, MessageChannel voteChannel, IGSQBot igsqBot)
	{
		this.options = options;
		this.users = users;
		this.timestamp = timestamp;
		this.subject = subject;
		this.voteChannel = voteChannel;
		this.igsqBot = igsqBot;
	}

	public void send()
	{

	}

	private MessageAction sendToVoteChannel()
	{
		StringBuilder options = new StringBuilder();
		users.forEach(user -> options.append(UserUtils.getAsMention(user)).append(" ").append("Not voted").append("\n"));
		return voteChannel.sendMessage(generateOptions()
				.addField("**Individual votes:**", options.toString(), false)
				.setColor(Constants.IGSQ_PURPLE).build());
	}

	private EmbedBuilder generateUserEmbed()
	{
		return generateOptions().addField("**Instructions: **", "Press the number on the option you would like to vote for. " +
				"You can press :negative_squared_cross_mark: to abstain.", false).setColor(Constants.IGSQ_PURPLE);
	}

	private EmbedBuilder generateOptions()
	{
		EmbedBuilder embed = new EmbedBuilder();
		int currentNumber = 1;

		embed.addField("**Subject: **", subject, false);

		for(String option : options)
		{
			embed.addField("**Option** " + currentNumber, option, false);
			currentNumber ++;
		}

		embed.addField("**Deadline: **", StringUtils.parseDateTime(timestamp), false);
		return embed;
	}

}
