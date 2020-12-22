package org.igsq.igsqbot.entities.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MessageDataCache
{
	private static final Map<String, MessageDataCache> STORED_DATA;

	static
	{
		STORED_DATA = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(30, TimeUnit.MINUTES)
				.build();
	}

	private final String messageId;
	private final JDA jda;
	private MessageType type;

	public MessageDataCache(String messageId, JDA jda)
	{
		this.messageId = messageId;
		this.jda = jda;
	}

	public static MessageDataCache getMessageData(String messageId, JDA jda)
	{
		if(STORED_DATA.get(messageId) != null)
		{
			return STORED_DATA.get(messageId);
		}
		else
		{
			MessageDataCache newData = new MessageDataCache(messageId, jda);
			if(!newData.getType().equals(MessageType.DISABLED))
			{
				return newData;
			}
			else
			{
				return null;
			}
		}
	}

	public static void close()
	{
		STORED_DATA.forEach((messageId, data) -> data.remove());
	}

	public MessageType getType()
	{
		if(Yaml.getFieldBool(messageId + ".report.enabled", "internal"))
		{
			type = MessageType.REPORT;
		}
		else if(Yaml.getFieldBool(messageId + ".help.enabled", "internal"))
		{
			type = MessageType.HELP;
		}
		else if(Yaml.getFieldBool(messageId + ".modhelp.enabled", "internal"))
		{
			type = MessageType.MODHELP;
		}
		else
		{
			type = MessageType.DISABLED;
		}
		return type;
	}

	public void setType(MessageType type)
	{
		switch(type)
		{
			case HELP:
			{
				Yaml.updateField(messageId + ".help.enabled", "internal", true);
				this.type = MessageType.HELP;
				break;
			}
			case REPORT:
			{
				Yaml.updateField(messageId + ".report.enabled", "internal", true);
				Yaml.updateField(messageId + ".report.enabled", "internal", true);
				this.type = MessageType.REPORT;
				break;
			}
			case MODHELP:
			{
				Yaml.updateField(messageId + ".modhelp.enabled", "internal", true);
				this.type = MessageType.MODHELP;
				break;
			}
			default:
			{
			}
		}
	}

	public Map<String, User> getUsers()
	{
		Map<String, User> result = new ConcurrentHashMap<>();
		switch(type)
		{
			case HELP:
			{
				result.put("user", jda.getUserById(Yaml.getFieldString(messageId + ".help.user", "internal")));
				break;
			}
			case REPORT:
			{
				result.put("reporteduser", jda.getUserById(Yaml.getFieldString(messageId + ".report.reporteduser", "internal")));
				result.put("reportinguser", jda.getUserById(Yaml.getFieldString(messageId + ".report.reportinguser", "internal")));
				break;
			}
			case MODHELP:
			{
				result.put("user", jda.getUserById(Yaml.getFieldString(messageId + ".modhelp.user", "internal")));
				break;
			}
			default:
			{
				return result;
			}
		}
		return result;
	}

	public Map<String, String> getUserIds()
	{
		Map<String, String> result = new ConcurrentHashMap<>();
		switch(type)
		{
			case HELP:
			{
				result.put("user", Yaml.getFieldString(messageId + ".help.user", "internal"));
				break;
			}
			case REPORT:
			{
				result.put("reporteduser", Yaml.getFieldString(messageId + ".report.reporteduser", "internal"));
				result.put("reportinguser", Yaml.getFieldString(messageId + ".report.reportinguser", "internal"));
				break;
			}
			case MODHELP:
			{
				result.put("user", Yaml.getFieldString(messageId + ".modhelp.user", "internal"));
				break;
			}
			default:
			{
				return result;
			}
		}
		return result;
	}

	public void setUsers(Map<String, String> userIds)
	{
		switch(type)
		{
			case HELP:
			{
				Yaml.updateField(messageId + ".help.user", "internal", userIds.get("user"));
				break;
			}
			case REPORT:
			{
				Yaml.updateField(messageId + ".report.reporteduser", "internal", userIds.get("reporteduser"));
				Yaml.updateField(messageId + ".report.reportinguser", "internal", userIds.get("reportinguser"));
				break;
			}
			case MODHELP:
			{
				Yaml.updateField(messageId + ".modhelp.user", "internal", userIds.get("user"));
				break;
			}
			default:
			{
			}
		}
	}

	public Map<String, Member> getMembers(Guild guild)
	{
		Map<String, Member> result = new ConcurrentHashMap<>();
		switch(type)
		{
			case HELP:
			{
				result.put("user", guild.getMemberById(Yaml.getFieldString(messageId + ".help.user", "internal")));
				break;
			}
			case REPORT:
			{
				result.put("reporteduser", guild.getMemberById(Yaml.getFieldString(messageId + ".report.reporteduser", "internal")));
				result.put("reportinguser", guild.getMemberById(Yaml.getFieldString(messageId + ".report.reportinguser", "internal")));
				break;
			}
			case MODHELP:
			{
				result.put("user", guild.getMemberById(Yaml.getFieldString(messageId + ".modhelp.user", "internal")));
				break;
			}
			default:
			{
				return result;
			}
		}
		return result;
	}

	public int getPage()
	{
		if(type.equals(MessageType.HELP))
		{
			return Yaml.getFieldInt(messageId + ".help.page", "internal");
		}
		else
		{
			return -1;
		}
	}

	public void setPage(int page)
	{
		if(type.equals(MessageType.HELP))
		{
			Yaml.updateField(messageId + ".help.page", "internal", page);
		}
	}

	public String getMessageId()
	{
		return messageId;
	}

	public void build()
	{
		STORED_DATA.put(messageId, this);
	}

	public void remove()
	{
		YamlUtils.clearField(messageId, "internal");
		STORED_DATA.remove(messageId);
	}

	public enum MessageType
	{
		REPORT,
		HELP,
		MODHELP,
		DISABLED;
	}
}
