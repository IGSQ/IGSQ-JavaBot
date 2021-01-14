package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

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

	private void addReactions(Message message)
	{
		for(int i = 1; i <= options.size(); i++)
		{
			message.addReaction(i + "\u20E3").queue();
		}
		message.addReaction("\u274E").queue();
	}

	private void addMessageToDatabase(Message message, long voteId)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(VOTES)
					.columns(VOTES.VOTEID, VOTES.OPTION, VOTES.MESSAGEID, VOTES.TIMESTAMP)
					.values(voteId, String.valueOf(options.size()), message.getIdLong(), timestamp);
			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static void addCast(long messageId, int option, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			igsqBot.getDatabaseManager().getContext(connection)
					.update(VOTES)
					.set(VOTES.OPTION, String.valueOf(option))
					.where(VOTES.MESSAGEID.eq(messageId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static int getMaxOption(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.select(VOTES.OPTION)
					.from(VOTES)
					.where(VOTES.MESSAGEID.eq(messageId));

			var result = context.fetchOne();
			if(result == null)
			{
				return -1;
			}
			else
			{

			}
			return Integer.parseInt(result.getValue(VOTES.OPTION));
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}
}
