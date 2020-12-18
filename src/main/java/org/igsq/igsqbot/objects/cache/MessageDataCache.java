package org.igsq.igsqbot.objects.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MessageDataCache
{
	private static final Map<String, MessageDataCache> STORED_DATA;
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDataCache.class);

	static
	{
		STORED_DATA = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.CREATED)
				.expiration(1, TimeUnit.HOURS)
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
		STORED_DATA.forEach((key, value) -> YamlUtils.clearField(key, "internal"));
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

	public List<User> getUsers()
	{
		List<User> result = new ArrayList<>();
		switch(type)
		{
			case HELP:
			{
				result.add(jda.getUserById(Yaml.getFieldString(messageId + ".help.user", "internal")));
				break;
			}
			case REPORT:
			{
				result.add(jda.getUserById(Yaml.getFieldString(messageId + ".report.reporteduser", "internal")));
				result.add(jda.getUserById(Yaml.getFieldString(messageId + ".report.reportinguser", "internal")));
				break;
			}
			case MODHELP:
			{
				result.add(jda.getUserById(Yaml.getFieldString(messageId + ".modhelp.user", "internal")));
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

	public List<Member> getMembers(Guild guild)
	{
		List<Member> result = new ArrayList<>();
		switch(type)
		{
			case HELP:
			{
				result.add(guild.getMemberById(Yaml.getFieldString(messageId + ".help.user", "internal")));
				break;
			}
			case REPORT:
			{
				result.add(guild.getMemberById(Yaml.getFieldString(messageId + ".report.reporteduser", "internal")));
				result.add(guild.getMemberById(Yaml.getFieldString(messageId + ".report.reportinguser", "internal")));
				break;
			}
			case MODHELP:
			{
				result.add(guild.getMemberById((Yaml.getFieldString(messageId + ".modhelp.user", "internal"))));
				break;
			}
			case DISABLED:
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

	public enum MessageType
	{
		REPORT,
		HELP,
		MODHELP,
		DISABLED;
	}
}
