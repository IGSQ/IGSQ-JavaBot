package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.jooq.Tables;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

import static org.igsq.igsqbot.entities.jooq.tables.Votes.VOTES;

public class Vote
{
	private final List<Long> users;
	private final List<String> options;
	private final LocalDateTime expiry;
	private final int maxOptions;
	private final IGSQBot igsqBot;
	private final CommandContext ctx;
	private final String subject;

	public Vote(List<Long> users, List<String> options, LocalDateTime expiry, String subject, CommandContext ctx)
	{
		this.users = users;
		this.options = options;
		this.expiry = expiry;
		this.subject = subject;
		this.maxOptions = options.size();
		this.igsqBot = ctx.getIGSQBot();
		this.ctx = ctx;
	}

	public void start()
	{
		MessageChannel voteChannel = ctx.getGuild().getTextChannelById(new GuildConfig(ctx).getVoteChannel());

		if(voteChannel == null)
		{
			ctx.replyError("Vote channel not setup");
			return;
		}

		voteChannel.sendMessage(generateGuildEmbed().build()).queue(
		message ->
		{
			message.editMessage(generateGuildEmbed().setFooter("Vote ID: " + message.getIdLong()).build()).queue();
			for(Long userId : users)
			{
				igsqBot.getShardManager()
						.retrieveUserById(userId)
						.flatMap(User::openPrivateChannel)
						.flatMap(channel -> channel.sendMessage(generateDMEmbed().build()))
						.queue(dm -> addUserToDatabase(userId, message.getIdLong(), dm.getIdLong()), error -> {});
			}
		});
	}

	private void addUserToDatabase(long userId, long voteId, long dmId)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.insertInto(Tables.VOTES)
					.columns(VOTES.VOTE_ID, VOTES.GUILD_ID, VOTES.USER_ID, VOTES.DIRECT_MESSAGE_ID, VOTES.MAX_OPTIONS, VOTES.EXPIRY, VOTES.OPTION)
					.values(voteId, ctx.getGuild().getIdLong(), userId, dmId, maxOptions, expiry, -1);
			query.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	private EmbedBuilder generateDMEmbed()
	{
		return generateOptions(new EmbedBuilder()
				.setTitle("You have been called to vote in " + ctx.getGuild().getName())
				.addField("Expires at", StringUtils.parseDateTime(expiry), false)
				.setColor(Constants.IGSQ_PURPLE)
				.setDescription("Reply to this message with `vote [option]` to cast your vote, if you do not respond, you will be considered abstained from this vote."));
	}

	private EmbedBuilder generateGuildEmbed()
	{
		return generateOptions(new EmbedBuilder()
				.setTitle(subject)
				.addField("Users", parseUserList(), false)
				.addField("Expires at", StringUtils.parseDateTime(expiry), false)
				.setColor(Constants.IGSQ_PURPLE));

	}

	private EmbedBuilder generateOptions(EmbedBuilder embed)
	{
		for(int i = 1; i < options.size() + 1; i++)
		{
			embed.addField("Option " + i, options.get(i - 1), true);
		}
		return embed;
	}

	private String parseUserList()
	{
		StringBuilder result = new StringBuilder();

		for(long user : users)
		{
			result.append(UserUtils.getAsMention(user)).append(" -> ").append("Not voted").append("\n");
		}
		return result.toString();
	}

	public static Boolean closeById(long voteId, CommandContext ctx)
	{
		try(Connection connection = ctx.getIGSQBot().getDatabaseHandler().getConnection())
		{
			var context = ctx.getIGSQBot().getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(Tables.VOTES)
					.where(VOTES.VOTE_ID.eq(voteId));

			var result = query.fetch();
			boolean exists = !result.isEmpty();

			if(!exists)
			{
				return false;
			}

			MessageChannel voteChannel = ctx.getGuild().getTextChannelById(new GuildConfig(ctx).getVoteChannel());

			if(voteChannel == null)
			{
				ctx.replyError("Vote channel not setup.");
				return null;
			}


			StringBuilder votes = new StringBuilder();
			for(var row : result)
			{
				votes.append(UserUtils.getAsMention(row.getUserId())).append(" -> ").append(parseOption(row.getOption())).append("\n");
			}

			voteChannel.retrieveMessageById(voteId).queue(message ->
					{
						List<MessageEmbed.Field> fields = new ArrayList<>(message.getEmbeds().get(0).getFields());
						String[] oldUsers = fields.get(0).getValue().split("\n");

						fields.removeIf(field -> !field.getName().startsWith("Option"));

						for(String user : oldUsers)
						{
							String mention = user.substring(0, user.indexOf(" "));
							if(!votes.toString().contains(mention))
							{
								votes.append(mention).append(" -> ").append("Abstained").append("\n");
							}
						}
						EmbedBuilder newEmbed = new EmbedBuilder(message.getEmbeds().get(0))
								.clearFields()
								.addField("Users", votes.toString(), false)
								.addField("Expires at:", "**Expired**", false);

						for(MessageEmbed.Field field : fields)
						{
							newEmbed.addField(field);
						}
						message.editMessage(newEmbed.build()).queue();
					}
					, error -> {});

			context.deleteFrom(Tables.VOTES).where(VOTES.VOTE_ID.eq(voteId)).execute();
			return true;
		}
		catch(Exception exception)
		{
			ctx.getIGSQBot().getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}

	public static void closeById(long voteId, long guildId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(Tables.VOTES)
					.where(VOTES.VOTE_ID.eq(voteId));

			var result = query.fetch();
			boolean exists = !result.isEmpty();

			if(!exists)
			{
				return;
			}

			MessageChannel voteChannel = igsqBot.getShardManager().getTextChannelById(new GuildConfig(guildId, igsqBot).getVoteChannel());

			if(voteChannel == null)
			{
				return;
			}


			StringBuilder votes = new StringBuilder();
			for(var row : result)
			{
				votes.append(UserUtils.getAsMention(row.getUserId())).append(" -> ").append(parseOption(row.getOption())).append("\n");
			}

			voteChannel.retrieveMessageById(voteId).queue(message ->
					{
						List<MessageEmbed.Field> fields = new ArrayList<>(message.getEmbeds().get(0).getFields());
						String[] oldUsers = fields.get(0).getValue().split("\n");

						fields.removeIf(field -> !field.getName().startsWith("Option"));

						for(String user : oldUsers)
						{
							String mention = user.substring(0, user.indexOf(" "));
							if(!votes.toString().contains(mention))
							{
								votes.append(mention).append(" -> ").append("Abstained").append("\n");
							}
						}
						EmbedBuilder newEmbed = new EmbedBuilder(message.getEmbeds().get(0))
								.clearFields()
								.addField("Users", votes.toString(), false)
								.addField("Expires at:", "**Expired**", false);

						for(MessageEmbed.Field field : fields)
						{
							newEmbed.addField(field);
						}
						message.editMessage(newEmbed.build()).queue();
					}
					, error -> {});

			context.deleteFrom(Tables.VOTES).where(VOTES.VOTE_ID.eq(voteId)).execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public static boolean castById(long messageId, int vote, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);

			var state = context.selectFrom(Tables.VOTES).where(VOTES.DIRECT_MESSAGE_ID.eq(messageId)).fetch();

			if(state.isEmpty() || state.get(0).getHasVoted())
			{
				return false;
			}

			var updateQuery = context.update(Tables.VOTES)
					.set(VOTES.HAS_VOTED, true)
					.set(VOTES.OPTION, vote);
			updateQuery.execute();
			return true;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return false;
		}
	}

	public static int getMaxVoteById(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);

			var query = context.selectFrom(Tables.VOTES).where(VOTES.DIRECT_MESSAGE_ID.eq(messageId));
			var result = query.fetch();
			if(result.isNotEmpty())
			{
				return result.get(0).getMaxOptions();
			}
			return -1;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return -1;
		}
	}

	private static String parseOption(int number)
	{
		return StringUtils.parseToEmote(number).isBlank() ? "Abstained" : " voted for option " + StringUtils.parseToEmote(number);
	}
}
