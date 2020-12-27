package org.igsq.igsqbot.entities.cache;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
			return new MessageDataCache(messageId, jda);
		}
	}

	public static void close()
	{
		STORED_DATA.forEach((messageId, data) -> data.remove());
	}

	public MessageType getType()
	{
		if(Yaml.getFieldBool(messageId + ".report.enabled", Filename.INTERNAL))
		{
			type = MessageType.REPORT;
		}
		else if(Yaml.getFieldBool(messageId + ".help.enabled", Filename.INTERNAL))
		{
			type = MessageType.HELP;
		}
		else if(Yaml.getFieldBool(messageId + ".modhelp.enabled", Filename.INTERNAL))
		{
			type = MessageType.MODHELP;
		}
		else if(Yaml.getFieldBool(messageId + ".verification.enabled", Filename.INTERNAL))
		{
			type = MessageType.VERIFICATION;
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
				Yaml.updateField(messageId + ".help.enabled", Filename.INTERNAL, true);
				this.type = MessageType.HELP;
				break;
			}
			case REPORT:
			{
				Yaml.updateField(messageId + ".report.enabled", Filename.INTERNAL, true);
				Yaml.updateField(messageId + ".report.enabled", Filename.INTERNAL, true);
				this.type = MessageType.REPORT;
				break;
			}
			case MODHELP:
			{
				Yaml.updateField(messageId + ".modhelp.enabled", Filename.INTERNAL, true);
				this.type = MessageType.MODHELP;
				break;
			}
			case VERIFICATION:
			{
				Yaml.updateField(messageId + ".verification.enabled", Filename.INTERNAL, true);
				this.type = MessageType.VERIFICATION;
				break;
			}
			default:
			{
			}
		}
	}

	public Map<String, User> getUsers()
	{
		if(type == null)
		{
			throw new UnsupportedOperationException("You must set the type before getting users");
		}
		Map<String, User> result = new ConcurrentHashMap<>();
		switch(type)
		{
			case HELP:
			{
				result.put("user", jda.getUserById(Yaml.getFieldString(messageId + ".help.user", Filename.INTERNAL)));
				break;
			}
			case REPORT:
			{
				result.put("reporteduser", jda.getUserById(Yaml.getFieldString(messageId + ".report.reporteduser", Filename.INTERNAL)));
				result.put("reportinguser", jda.getUserById(Yaml.getFieldString(messageId + ".report.reportinguser", Filename.INTERNAL)));
				break;
			}
			case MODHELP:
			{
				result.put("user", jda.getUserById(Yaml.getFieldString(messageId + ".modhelp.user", Filename.INTERNAL)));
				break;
			}
			case VERIFICATION:
				result.put("author", jda.getUserById(Yaml.getFieldString(messageId + ".verification.author", Filename.INTERNAL)));
				result.put("target", jda.getUserById(Yaml.getFieldString(messageId + ".verification.target", Filename.INTERNAL)));
				break;
			default:
			{
				return result;
			}
		}
		return result;
	}

	public Map<String, String> getUserIds()
	{
		if(type == null)
		{
			throw new UnsupportedOperationException("You must set the type before getting users");
		}
		Map<String, String> result = new ConcurrentHashMap<>();
		switch(type)
		{
			case HELP:
			{
				result.put("user", Yaml.getFieldString(messageId + ".help.user", Filename.INTERNAL));
				break;
			}
			case REPORT:
			{
				result.put("reporteduser", Yaml.getFieldString(messageId + ".report.reporteduser", Filename.INTERNAL));
				result.put("reportinguser", Yaml.getFieldString(messageId + ".report.reportinguser", Filename.INTERNAL));
				break;
			}
			case MODHELP:
			{
				result.put("user", Yaml.getFieldString(messageId + ".modhelp.user", Filename.INTERNAL));
				break;
			}
			case VERIFICATION:
				result.put("author", Yaml.getFieldString(messageId + ".verification.author", Filename.INTERNAL));
				result.put("target", Yaml.getFieldString(messageId + ".verification.target", Filename.INTERNAL));
				break;
			default:
			{
				return result;
			}
		}
		return result;
	}

	public void setUsers(Map<String, String> userIds)
	{
		if(type == null)
		{
				throw new UnsupportedOperationException("You must set the type before setting users");
		}
		switch(type)
		{
			case HELP:
			{
				Yaml.updateField(messageId + ".help.user", Filename.INTERNAL, userIds.get("user"));
				break;
			}
			case REPORT:
			{
				Yaml.updateField(messageId + ".report.reporteduser", Filename.INTERNAL, userIds.get("reporteduser"));
				Yaml.updateField(messageId + ".report.reportinguser", Filename.INTERNAL, userIds.get("reportinguser"));
				break;
			}
			case MODHELP:
			{
				Yaml.updateField(messageId + ".modhelp.user", Filename.INTERNAL, userIds.get("user"));
				break;
			}
			case VERIFICATION:
				Yaml.updateField(messageId + ".verification.author", Filename.INTERNAL, userIds.get("author"));
				Yaml.updateField(messageId + ".verification.target", Filename.INTERNAL, userIds.get("target"));
				break;
			default:
			{
			}
		}
	}

	public void setRoles(Map<String, String> roles)
	{
		if(type == null)
		{
			throw new UnsupportedOperationException("You must set the type before setting roles");
		}
		switch(type)
		{
			case HELP:
			case REPORT:
			case MODHELP:
			{
				break;
			}
			case VERIFICATION:
				Yaml.updateField(messageId + ".verification.guessedroles", Filename.INTERNAL, roles.get("guess"));
				Yaml.updateField(messageId + ".verification.matchedroles", Filename.INTERNAL, roles.get("match"));
				break;
			default:
			{
			}
		}
	}

	public Map<Role, String> getRoles(Guild guild)
	{
		if(type == null)
		{
			throw new UnsupportedOperationException("You must set the type before getting roles");
		}
		Map<Role, String> result = new ConcurrentHashMap<>();
		switch(type)
		{
			case HELP:
			case MODHELP:
			case REPORT:
			{
				break;
			}
			case VERIFICATION:
				Arrays.stream(Yaml.getFieldString(messageId + ".verification.guessedroles", Filename.INTERNAL)
						.split("/"))
						.map(guild::getRoleById)
						.filter(Objects::nonNull)
						.forEach(role -> result.put(role, "guess"));

				Arrays.stream(Yaml.getFieldString(messageId + ".verification.matchedroles", Filename.INTERNAL)
						.split("/"))
						.map(guild::getRoleById)
						.filter(Objects::nonNull)
						.forEach(role -> result.put(role, "match"));
				break;
			default:
			{
				return result;
			}
		}
		return result;
	}

	public Map<String, Member> getMembers(Guild guild)
	{
		if(type == null)
		{
			throw new UnsupportedOperationException("You must set the type before setting members");
		}
		Map<String, Member> result = new ConcurrentHashMap<>();
		switch(type)
		{
			case HELP:
			{
				result.put("user", guild.getMemberById(Yaml.getFieldString(messageId + ".help.user", Filename.INTERNAL)));
				break;
			}
			case REPORT:
			{
				result.put("reporteduser", guild.getMemberById(Yaml.getFieldString(messageId + ".report.reporteduser", Filename.INTERNAL)));
				result.put("reportinguser", guild.getMemberById(Yaml.getFieldString(messageId + ".report.reportinguser", Filename.INTERNAL)));
				break;
			}
			case MODHELP:
			{
				result.put("user", guild.getMemberById(Yaml.getFieldString(messageId + ".modhelp.user", Filename.INTERNAL)));
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
		if(type == null || !type.equals(MessageType.HELP))
		{
			throw new UnsupportedOperationException("The type must be HELP to get the page.");
		}
		else
		{
			return Yaml.getFieldInt(messageId + ".help.page", Filename.INTERNAL);
		}
	}

	public void setPage(int page)
	{
		if(type == null || !type.equals(MessageType.HELP))
		{
			throw new UnsupportedOperationException("The type must be HELP to set the page.");
		}
		else
		{
			Yaml.updateField(messageId + ".help.page", Filename.INTERNAL, page);
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
		YamlUtils.clearField(messageId, Filename.INTERNAL);
		STORED_DATA.remove(messageId);
	}

	public enum MessageType
	{
		REPORT,
		HELP,
		MODHELP,
		VERIFICATION,
		DISABLED;
	}
}
