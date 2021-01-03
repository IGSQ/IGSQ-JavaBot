package org.igsq.igsqbot.entities.json;

import com.google.gson.JsonObject;
import org.igsq.igsqbot.entities.cache.PunishmentCache;

import java.util.List;

public class Report implements IJsonEntity
{
	private final String reportedUser;
	private final String guildId;
	private String messageId;
	private String reportingUser;

	public String getGuildId()
	{
		return guildId;
	}

	public String getReportedUser()
	{
		return reportedUser;
	}

	public Report(String messageId, String guildId, String reportingUser, String reportedUser)
	{
		this.messageId = messageId;
		this.guildId = guildId;
		this.reportingUser = reportingUser;
		this.reportedUser = reportedUser;
	}

	public String getMessageId()
	{
		return messageId;
	}

	public void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}

	public String getReportingUser()
	{
		return reportingUser;
	}

	public void setReportingUser(String reportingUser)
	{
		this.reportingUser = reportingUser;
	}

	@Override
	public JsonObject toJson()
	{
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("guildId", guildId);
		jsonObject.addProperty("messageId", messageId);
		jsonObject.addProperty("reportingUser", reportingUser);
		jsonObject.addProperty("reportedUser", reportedUser);
		return jsonObject;
	}

	@Override
	public String getPrimaryKey()
	{
		return messageId;
	}

	@Override
	public void remove()
	{
		Punishment punishment = PunishmentCache.getInstance().get(guildId, reportedUser);
		List<Report> reports = punishment.getReports();
		reports.removeIf(report -> report.getPrimaryKey().equals(getPrimaryKey()));
		punishment.setReports(reports);
	}
}
