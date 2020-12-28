package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VerificationCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Guild guild = ctx.getGuild();
		final List<String> messageContent = new ArrayList<>();
		final StringBuilder embedText = new StringBuilder();
		final Message message = ctx.getMessage();

		if(args.size() != 1 || message.getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			final Member verificationTarget = message.getMentionedMembers().get(0);

			channel.getHistory().retrievePast(10).queue(messages ->
			{
				messages.stream()
						.filter(channelMessage -> channelMessage.getAuthor().equals(verificationTarget.getUser()))
						.forEach(channelMessage -> messageContent.addAll(Arrays.asList(channelMessage.getContentRaw().split(" "))));

				final Map<String, String> roleMap = CommandUtils.getAliases(guild.getId());

				final Map<String, String> matches = intersectMaps(findMatches(messageContent, roleMap), CommandUtils.getDeclined(guild.getId()));
				final Map<String, String> similar = findSimilar(messageContent, roleMap, matches);

				final StringBuilder matchBuilder = new StringBuilder();
				final StringBuilder similarBuilder = new StringBuilder();

				matches.values().stream().map(guild::getRoleById).filter(Objects::nonNull).forEach(role ->
						{
							embedText.append("Detected Role: ")
									.append(role.getAsMention())
									.append(" (Matched)\n");

							matchBuilder.append(role.getId()).append("/");
						});



				embedText.append("\n");
				similar.forEach((alias, roleId) ->
				{
					final Role role = guild.getRoleById(roleId);
					if(role != null)
					{
						embedText.append("Detected Role: ")
								.append(role.getAsMention())
								.append(" (Guess)\n");
						similarBuilder.append(role.getId()).append("/");
					}
				});

				channel.sendMessage(new EmbedGenerator(channel)
						.title("Verification for: " + verificationTarget.getUser().getAsTag())
						.text(embedText.length() == 0 ? "No roles found for this user." : embedText.toString())
						.getBuilder().build()).queue(
								verificationMessage ->
								{
									Constants.THUMB_REACTIONS.forEach(reaction -> verificationMessage.addReaction(reaction).queue());

									final MessageDataCache dataCache = MessageDataCache.getMessageData(verificationMessage.getId(), ctx.getJDA());
									final Map<String, String> users = new ConcurrentHashMap<>();
									final Map<String, String> roles = new ConcurrentHashMap<>();

									users.put("author", ctx.getAuthor().getId());
									users.put("target", verificationTarget.getId());

									roles.put("match", matchBuilder.toString());
									roles.put("guess", similarBuilder.toString());

									dataCache.setType(MessageDataCache.MessageType.VERIFICATION);
									dataCache.setUsers(users);
									dataCache.setRoles(roles);
									dataCache.build();
								}
				);
			});
		}
	}

	@Override
	public String getName()
	{
		return "Verify";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("verify", "v", "accept");
	}

	@Override
	public String getDescription()
	{
		return "Verifies the specified user into the server";
	}

	@Override
	public String getSyntax()
	{
		return "[user]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}

	private Map<String, String> findMatches(List<String> words, Map<String, String> aliases)
	{
		Map<String, String> result = new ConcurrentHashMap<>();
		aliases.keySet().stream().filter(words::contains).forEach(alias -> result.putIfAbsent(alias, aliases.get(alias)));
		return result;
}

	private Map<String, String> intersectMaps(Map<String, String> map, Map<String, String> anotherMap)
	{
		Map<String, String> result = new ConcurrentHashMap<>();
		map.keySet().stream().filter(alias -> !anotherMap.containsKey(alias)).forEach(finalAlias -> result.putIfAbsent(finalAlias, map.get(finalAlias)));
		return result;
	}

	private Map<String, String> findSimilar(List<String> words, Map<String,String> roleList, Map<String, String> existingMatches)
	{
		Map<String, String> result = new ConcurrentHashMap<>();
		words.removeIf(existingMatches.keySet()::contains);

		words.forEach(selectedWord ->
		{
			roleList.forEach((selectedAlias, selectedRole) ->
			{
				if(StringUtils.isOption(selectedAlias, selectedWord, 10))
				{
					result.putIfAbsent(selectedAlias, selectedRole);
				}
			});
		});
		return result;
	}

}
