package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VerificationCommand extends Command
{
	public VerificationCommand()
	{
		super("Verify", new String[]{"verify", "v", "accept"}, "Verifies the specified user into the server", "[user]", new Permission[]{}, true, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Guild guild = ctx.getGuild();
		final User verificationTarget;
		final List<String> messageContent = new ArrayList<>();
		final StringBuilder embedText = new StringBuilder();

		try
		{
			verificationTarget = UserUtils.getUserFromMention(args.get(0));
		}
		catch(Exception exception)
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}

		if(args.size() != 1)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}

		channel.getHistory().retrievePast(10).queue(messages ->
				messages.stream().filter(message -> message.getAuthor().equals(verificationTarget)).forEach(message -> messageContent.addAll(Arrays.asList(message.getContentRaw().split(" ")))));

		final Map<String, String> roleMap = CommandUtils.getAliases(guild.getId());

		final Map<String, String> matches = intersectMaps(findMatches(messageContent, roleMap), CommandUtils.getDeclined(guild.getId()));
		final Map<String, String> similar = findSimilar(messageContent, roleMap, matches);

		matches.values().stream().map(guild::getRoleById).forEach(role ->
				embedText.append("Detected Role: ")
						.append(role.getAsMention())
						.append(" (Matched)\n"));

		embedText.append("\n");
		similar.forEach((alias, roleId) ->
		{
			final Role role = guild.getRoleById(roleId);
			embedText.append("Detected Role: ")
					.append(role.getAsMention())
					.append(" (Guess)\n");
		});

		new EmbedGenerator(channel).text(embedText.toString()).send();

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
