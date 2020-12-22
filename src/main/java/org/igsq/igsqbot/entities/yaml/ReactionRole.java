package org.igsq.igsqbot.entities.yaml;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ReactionRole
{
	private final String guildId;
	private final String channelId;
	private final String messageId;
	private static final Logger LOGGER = LoggerFactory.getLogger(ReactionRole.class);

	public ReactionRole(String guildId, String channelId, String messageId)
	{
		this.guildId = guildId;
		this.channelId = channelId;
		this.messageId = messageId;
	}

	public void setReaction(String emoji, String roleId)
	{
		Yaml.updateField(guildId + ".reactionroles." + channelId + "." + messageId, "guild", YamlUtils.getFieldAppended(guildId + ".reactionroles." + channelId + "." + messageId, "guild", " ", roleId + "/" + emoji));
	}

	public void setReaction(Emote emote, String roleId)
	{
		Yaml.updateField(guildId + ".reactionroles." + channelId + "." + messageId, "guild", YamlUtils.getFieldAppended(guildId + ".reactionroles." + channelId + "." + messageId, "guild", " ", roleId + "/" + emote.getId()));
	}

	public List<String> getReaction(String roleId)
	{
		List<String> result = new ArrayList<>();
		if(YamlUtils.isFieldEmpty(guildId + ".reactionroles." + channelId + "." + messageId, "guild"))
		{
			return result;
		}
		else
		{
			Arrays.stream(Yaml.getFieldString(guildId + ".reactionroles." + channelId + "." + messageId, "guild").split(" "))
					.filter(roleId::contains)
					.map(role -> role.split("/")[1])
					.forEach(result::add);
		}
		return result;
	}

	public List<Role> getRoles(String emoteId, Guild guild)
	{
		List<Role> result = new ArrayList<>();
		if(YamlUtils.isFieldEmpty(guildId + ".reactionroles." + channelId + "." + messageId, "guild"))
		{
			return result;
		}
		else
		{
			Arrays.stream(Yaml.getFieldString(guildId + ".reactionroles." + channelId + "." + messageId, "guild").split(" "))
					.filter(pair -> pair.contains(emoteId))
					.map(pair -> pair.split("/")[0])
					.map(guild::getRoleById)
					.filter(Objects::nonNull)
					.forEach(result::add);
		}
		return result;
	}

	public void removeReaction(String emoji, String roleId)
	{
		StringBuilder dependedString = new StringBuilder();
		Arrays.stream(Yaml.getFieldString(guildId + ".reactionroles." + channelId + "." + messageId, "guild").split(" "))
				.filter(pair -> !pair.matches(roleId + "/" + emoji))
				.forEach(dependedString::append);
		Yaml.updateField(guildId + ".reactionroles." + channelId + "." + messageId, "guild", dependedString.toString());
	}

	public void removeReaction(Emote emote, String roleId)
	{
		StringBuilder dependedString = new StringBuilder();
		Arrays.stream(Yaml.getFieldString(guildId + ".reactionroles." + channelId + "." + messageId, "guild").split(" "))
				.filter(pair -> !pair.matches(roleId + "/" + emote.getId()))
				.forEach(dependedString::append);
		Yaml.updateField(guildId + ".reactionroles." + channelId + "." + messageId, "guild", dependedString.toString());
	}

	public boolean isEmojiMapped(String emoji)
	{
		if(YamlUtils.isFieldEmpty(guildId + ".reactionroles." + channelId + "." + messageId, "guild"))
		{
			return false;
		}
		List<String> matches = new ArrayList<>();
		Arrays.stream(Yaml.getFieldString(guildId + ".reactionroles." + channelId + "." + messageId, "guild").split(" "))
				.map(pair -> pair.split("/")[1])
				.filter(emoji::matches)
				.forEach(matches::add);

		return !matches.isEmpty();
	}

	public boolean isEmoteMapped(Emote emote)
	{
		if(YamlUtils.isFieldEmpty(guildId + ".reactionroles." + channelId + "." + messageId, "guild"))
		{
			return false;
		}

		List<String> matches = new ArrayList<>();
		Arrays.stream(Yaml.getFieldString(guildId + ".reactionroles." + channelId + "." + messageId, "guild").split(" "))
				.map(pair -> pair.split("/")[1])
				.filter(emote.getId()::matches)
				.forEach(matches::add);

		return !matches.isEmpty();
	}

	public String getMessageId()
	{
		return messageId;
	}

	public String getChannelId()
	{
		return channelId;
	}

	public String getGuildId()
	{
		return guildId;
	}
}
