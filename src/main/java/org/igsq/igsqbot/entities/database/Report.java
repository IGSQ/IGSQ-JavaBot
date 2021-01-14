package org.igsq.igsqbot.entities.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;

import static org.igsq.igsqbot.entities.jooq.tables.Reports.REPORTS;

public class Report
{
	private final long messageId;
	private final long commandMessageId;
	private final long channelId;
	private final long guildId;
	private final long reportedUserId;
	private final String reason;
	private final IGSQBot igsqBot;
	private final Timestamp timestamp;

	public Report(Message message, Message commandMessage, MessageChannel channel, Guild guild, User reportedUser, String reason, IGSQBot igsqBot)
	{
		this.messageId = message.getIdLong();
		this.commandMessageId = commandMessage.getIdLong();
		this.channelId = channel.getIdLong();
		this.guildId = guild.getIdLong();
		this.reportedUserId = reportedUser.getIdLong();
		this.timestamp = Timestamp.from(Instant.now());
		this.reason = reason;
		this.igsqBot = igsqBot;
	}

	public Report(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, Timestamp timestamp, String reason, IGSQBot igsqBot)
	{
		this.messageId = messageId;
		this.commandMessageId = commandMessageId;
		this.channelId = channelId;
		this.guildId = guildId;
		this.reportedUserId = reportedUserId;
		this.timestamp = timestamp;
		this.reason = reason;
		this.igsqBot = igsqBot;
	}

	public void add()
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.insertInto(Tables.REPORTS)
					.columns(REPORTS.MESSAGEID, REPORTS.REPORTMESSAGEID, REPORTS.CHANNELID, REPORTS.GUILDID, REPORTS.USERID, REPORTS.REPORTTEXT)
					.values(messageId, commandMessageId, channelId, guildId, reportedUserId, reason);

			context.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public void remove()
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			var context = igsqBot.getDatabaseManager().getContext(connection)
					.deleteFrom(Tables.REPORTS)
					.where(REPORTS.MESSAGEID.eq(messageId));

			context.execute();
			context.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public long getMessageId()
	{
		return messageId;
	}

	public long getCommandMessageId()
	{
		return commandMessageId;
	}

	public long getChannelId()
	{
		return channelId;
	}

	public long getGuildId()
	{
		return guildId;
	}

	public long getReportedUserId()
	{
		return reportedUserId;
	}

	public String getReason()
	{
		return reason;
	}

	public IGSQBot getIgsqBot()
	{
		return igsqBot;
	}

	public Timestamp getTimestamp()
	{
		return timestamp;
	}

	public static Report getById(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseManager().getConnection())
		{
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM reports WHERE messageId = ?");
			statement.setLong(1, messageId);

			if(statement.execute())
			{
				ResultSet resultSet = statement.getResultSet();
				if(resultSet.next())
				{
					return new Report(resultSet.getLong(2), resultSet.getLong(3), resultSet.getLong(4), resultSet.getLong(5), resultSet.getLong(6), resultSet.getTimestamp(7), resultSet.getString(8), igsqBot);
				}
			}
			return null;
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}
}
