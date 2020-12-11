package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.util.Command_Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Verification_Command extends Command
{
	private final StringBuilder embedText = new StringBuilder();
	private String messageContent;

	public Verification_Command()
	{
		super("verify", new String[]{"v", "accept"}, "Verifies the specified user into the server", new Permission[]{}, true, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		//Compare each word + each 2 word to aliases
		// Class variable string builder
		messageContent = ctx.getMessage().getContentRaw();
		Guild guild = ctx.getGuild();

		String[] retrievedRoles = Command_Utils.getRoles(guild.getId());
		Map<String, String> aliasMatches = findMatches(Command_Utils.getAliases(guild.getId()), retrievedRoles);
		Map<String, String> declineMatches = findMatches(Command_Utils.getDeclined(guild.getId()), retrievedRoles);


		aliasMatches.forEach((key, value) ->
		{
			if(messageContent.contains(key))
			{
				aliasMatches.remove(key, value);
			}
		});



	}

	private Map<String, String> findMatches(String[][] input, String[] roles)
	{
		Map<String, String> result = new HashMap<>();
		int currentRole = 0;
		for(String[] aliasCollection : input)
		{
			Arrays.stream(aliasCollection).filter(word -> messageContent.contains(word)).map(word -> result.putIfAbsent(word, roles[currentRole])).close();
		}
		return result;
	}
}
