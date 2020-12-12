package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Verification_Command extends Command
{
	private String[] messageContent = new String[0];

	public Verification_Command()
	{
		super("verify", new String[]{"v", "accept"}, "Verifies the specified user into the server", new Permission[]{}, true, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final StringBuilder embedText = new StringBuilder();
		final MessageChannel channel = ctx.getChannel();
		final Guild guild = ctx.getGuild();
		final User verificationTarget;

		final String[] retrievedRoles = Command_Utils.getRoles(guild.getId());

		try
		{
			verificationTarget = User_Utils.getUserFromMention(args[0]);
		}
		catch(Exception exception)
		{
			Embed_Utils.sendError(channel, "Enter a user to verify!");
			return;
		}

		channel.getHistory().retrievePast(10).queue(
			messages ->
			{
				messages.forEach(
				message ->
				{
					if(message.getAuthor().equals(verificationTarget))
					{
						messageContent = Array_Utils.append(messageContent, message.getContentRaw() + " ");
					}
				});


				final Map<String, String> aliasMatches = findMatches(Command_Utils.getAliases(guild.getId()), retrievedRoles);
				final Map<String, String> declineMatches = findMatches(Command_Utils.getDeclined(guild.getId()), retrievedRoles);

				aliasMatches.forEach((key, value) ->
				{
					if(declineMatches.containsKey(key))
					{
						aliasMatches.remove(key, value);
					}
				});

				aliasMatches.forEach((alias, role) ->
						embedText.append("Detected Role: ")
								.append(User_Utils.getRoleAsMention(role))
								.append(" (Sure)")
								.append("\n")
				);

				findSimilar(Command_Utils.getAliases(guild.getId()), retrievedRoles).forEach((alias, role) ->
						embedText.append("Detected Role: ")
						.append(User_Utils.getRoleAsMention(role))
						.append(" (Guess) for '")
						.append(alias)
						.append("'\n"));

				new EmbedGenerator(channel)
						.title("Verification for " + verificationTarget.getAsTag())
						.text(embedText.length() == 0 ? "No roles found for this user." : embedText.toString())
						.color(Common.IGSQ_PURPLE)
						.reaction(Common.TICK_REACTIONS.toArray(new String[0]))
						.send();
			}
		);
	}

	private Map<String, String> findMatches(String[][] input, String[] roles)
	{
		int currentRole = 0;
		Map<String, String> result = new HashMap<>();
		for(String[] aliasCollection : input)
		{
			final int finalCurrentRole = currentRole;
			Arrays.stream(aliasCollection)
					.filter(word -> Array_Utils.isValueInArray(messageContent, word))
					.forEach(word ->
					{
						result.putIfAbsent(word, roles[finalCurrentRole]);
						messageContent = (String[]) Array_Utils.depend(messageContent, word);
					});
			currentRole++;
		}
		return result;
	}

	private Map<String, String> findSimilar(String[][] input, String[] roles)
	{
		int currentRole = 0;
		Map<String, String> result = new HashMap<>();
		for(String[] aliasCollection : input)
		{
			final int finalCurrentRole = currentRole;
			Arrays.stream(aliasCollection)
					.forEach(alias ->
					Arrays.stream(messageContent)
						.forEach(messageWord ->
						{
							if(String_Utils.isOption(alias, messageWord, 10))
							{
								result.putIfAbsent(messageWord, roles[finalCurrentRole]);
							}
						}
					));
			currentRole++;
		}
		return result;
	}
}
