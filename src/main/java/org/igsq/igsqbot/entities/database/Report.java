package org.igsq.igsqbot.entities.database;

import java.sql.Connection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.jooq.Tables;

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

	public Report(Message message, Message commandMessage, MessageChannel channel, Guild guild, User reportedUser, String reason, IGSQBot igsqBot)
	{
		this.messageId = message.getIdLong();
		this.commandMessageId = commandMessage.getIdLong();
		this.channelId = channel.getIdLong();
		this.guildId = guild.getIdLong();
		this.reportedUserId = reportedUser.getIdLong();
		this.reason = reason;
		this.igsqBot = igsqBot;
	}

	public Report(long messageId, long commandMessageId, long channelId, long guildId, long reportedUserId, String reason, IGSQBot igsqBot)
	{
		this.messageId = messageId;
		this.commandMessageId = commandMessageId;
		this.channelId = channelId;
		this.guildId = guildId;
		this.reportedUserId = reportedUserId;
		this.reason = reason;
		this.igsqBot = igsqBot;
	}

	public static Report getById(long messageId, IGSQBot igsqBot)
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var context = igsqBot.getDatabaseHandler().getContext(connection);
			var query = context.selectFrom(REPORTS).where(REPORTS.MESSAGEID.eq(messageId));
			var result = query.fetch();
			query.close();

			if(!result.isEmpty())
			{
				var report = result.get(0);
				return new Report(report.getMessageid(), report.getReportmessageid(), report.getChannelid(), report.getGuildid(), report.getUserid(), report.getReporttext(), igsqBot);
			}
			else
			{
				return null;
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
			return null;
		}
	}

	public void add()
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);

			ctx.insertInto(Tables.REPORTS)
					.columns(REPORTS.MESSAGEID, REPORTS.REPORTMESSAGEID, REPORTS.CHANNELID, REPORTS.GUILDID, REPORTS.USERID, REPORTS.REPORTTEXT)
					.values(messageId, commandMessageId, channelId, guildId, reportedUserId, reason)
					.execute();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An SQL error occurred", exception);
		}
	}

	public void remove()
	{
		try(Connection connection = igsqBot.getDatabaseHandler().getConnection())
		{
			var ctx = igsqBot.getDatabaseHandler().getContext(connection);
			ctx.deleteFrom(Tables.REPORTS).where(REPORTS.MESSAGEID.eq(messageId)).execute();
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
}
