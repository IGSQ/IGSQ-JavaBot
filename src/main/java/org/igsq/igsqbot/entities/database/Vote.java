package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.util.StringUtils;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.igsq.igsqbot.entities.jooq.tables.Votes.VOTES;

public class Vote
{
	private final List<Long> users;
	private final List<String> options;
	private final IGSQBot igsqBot;
	private static final String NUMBER_CODEPOINT = "\uFE0F";
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
		EmbedBuilder embed = generateUserEmbed();
		sendToVoteChannel().queue(
				voteMessage ->
					users.stream()
							.map(user -> igsqBot.getShardManager().getUserById(user))
							.filter(Objects::nonNull)
							.map(User::openPrivateChannel)
							.forEach(restAction -> restAction
									.flatMap(channel -> channel.sendMessage(embed.build()))
									.queue(message ->
									{
										addReactions(message);
										addMessageToDatabase(message, voteMessage.getIdLong());

									}))
		);
	}

	private MessageAction sendToVoteChannel()
	{
		EmbedBuilder embed = generateOptions();
		embed.addField("**Individual votes:**", "", false);
		return voteChannel.sendMessage(embed.build());
	}

	private EmbedBuilder generateUserEmbed()
	{
		EmbedBuilder embed = generateOptions();
		embed.addField("**Instructions: **", "Press the number on the option you would like to vote for. " +
				"You can press :negative_squared_cross_mark: to abstain.", false);
		return embed;
	}

	private EmbedBuilder generateOptions()
	{
		EmbedBuilder embed = new EmbedBuilder();
		StringBuilder embedText = new StringBuilder();
		int currentNumber = 1;

		embed.addField("**Subject: **", subject, false);

		for(String option : options)
		{
			embedText.append("*Option ")
					.append(currentNumber)
					.append(NUMBER_CODEPOINT)
					.append("*")
					.append("\n")
					.append(option);
			currentNumber ++;
		}

		embed.addField("**Deadline: **", StringUtils.parseDateTime(timestamp), false);
		embed.addField("**Options: **", embedText.toString(), false);
		return embed;
	}

	private void addReactions(Message message)
	{
		for(int i = 0; i < options.size(); i++)
		{
			message.addReaction(i + NUMBER_CODEPOINT).queue();
		}
		message.addReaction("\u274E").queue();
	}

	private void addMessageToDatabase(Message message, long voteId)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(VOTES)
					.columns(VOTES.VOTEID, VOTES.MESSAGEID, VOTES.TIMESTAMP)
					.values(voteId, message.getIdLong(), timestamp);

			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}
}
