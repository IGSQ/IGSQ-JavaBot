package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.EventWaiter;
import org.igsq.igsqbot.util.*;

import java.util.*;

public class Verification_Command extends Command
{
	private final List<String> messageContent = new ArrayList<>();

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
		final User author = ctx.getAuthor();
		final JDA jda = ctx.getJDA();

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
				selectedMessage ->
				{
					if(selectedMessage.getAuthor().equals(verificationTarget) && !selectedMessage.getContentRaw().startsWith(Common.BOT_PREFIX))
					{
						messageContent.addAll(Arrays.asList(selectedMessage.getContentRaw().split(" ")));
					}
				});


				final Map<String, String> aliasMatches = findMatches(Command_Utils.getAliases(guild.getId()));
				final Map<String, String> declineMatches = findMatches(Command_Utils.getDeclined(guild.getId()));

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

				final Map<String, String> similarMatches = findSimilar(Command_Utils.getAliases(guild.getId()), aliasMatches.values().toArray(new String[0]));
				similarMatches.forEach((alias, role) ->
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
						.sendTemporary();

				EventWaiter waiter = new EventWaiter();
				MessageReactionAddEvent reactionAddEvent;
				try
				{
					reactionAddEvent = waiter.waitFor(MessageReactionAddEvent.class, event -> event.getUser().equals(author) && !event.getUser().isBot(), 10000);
				}
				catch(Exception exception)
				{
					reactionAddEvent = null;
				}

				if(reactionAddEvent != null)
				{
					final MessageChannel welcomeChannel = jda.getTextChannelById(Yaml.getFieldString(guild.getId() + ".welcomechannel", "guild"));
					final StringBuilder roleText = new StringBuilder();
					final List<Role> finalRoles = new ArrayList<>();
					long verificationMember = User_Utils.getMemberFromUser(verificationTarget, guild).getIdLong();

					aliasMatches.values().forEach(id -> finalRoles.add(guild.getRoleById(id)));

					if(reactionAddEvent.getReactionEmote().getAsCodepoints().equalsIgnoreCase(Common.TICK_REACTIONS.get(0)))
					{
						similarMatches.values().forEach(id -> finalRoles.add(guild.getRoleById(id)));

						for(Role selectedRole : finalRoles)
						{
							if(selectedRole != null)
							{
								guild.addRoleToMember(verificationMember, selectedRole).queue();
								roleText.append(selectedRole.getAsMention()).append(" ");
							}
						}

						if(welcomeChannel == null)
						{
							Embed_Utils.sendError(channel, "There is no welcome channel setup, defaulting to role only verification.");
						}
						else
						{
							new EmbedGenerator(welcomeChannel)
									.title(verificationTarget.getAsTag())
									.text(verificationTarget.getAsMention() + " has joined the " + guild.getName())
									.color(Common.IGSQ_PURPLE)
									.element("Roles", embedText.length() > 0 ? roleText.toString() : "No roles.");
						}
					}
					else if(reactionAddEvent.getReactionEmote().getAsCodepoints().equalsIgnoreCase(Common.TICK_REACTIONS.get(1)))
					{
						for(Role selectedRole : finalRoles)
						{
							if(selectedRole != null)
							{
								guild.addRoleToMember(verificationMember, selectedRole).queue();
								roleText.append(selectedRole.getAsMention()).append(" ");
							}
						}

						if(welcomeChannel == null)
						{
							Embed_Utils.sendError(channel, "There is no welcome channel setup, defaulting to role only verification.");
						}
						else
						{
							new EmbedGenerator(welcomeChannel)
									.title(verificationTarget.getAsTag())
									.text(verificationTarget.getAsMention() + " has joined the " + guild.getName())
									.color(Common.IGSQ_PURPLE)
									.element("Roles", embedText.length() > 0 ? roleText.toString() : "No roles.");
						}
					}
				}
			}
		);
	}

	private Map<String, String> findMatches(String[][] input)
	{
		Map<String, String> result = new HashMap<>();
		for(String[] aliasCollection : input)
		{
			for(String selectedAlias : aliasCollection)
			{
				if(!result.containsValue(aliasCollection[0]) && messageContent.contains(selectedAlias))
				{
					result.putIfAbsent(selectedAlias, aliasCollection[0]);
					messageContent.remove(selectedAlias);
				}
			}
		}
		return result;
	}

	private Map<String, String> findSimilar(String[][] aliases, String[] matchedRoles)
	{
		Map<String, String> result = new HashMap<>();

		for(String[] aliasCollection : aliases)
		{
			Arrays.stream(aliasCollection)
					.filter(alias -> !messageContent.contains(alias))
					.forEach(currentAlias ->
					{
						messageContent.forEach(currentWord ->
							{
								if(!Array_Utils.isValueInArray(matchedRoles, aliasCollection[0]) && String_Utils.isOption(currentAlias,currentWord,10))
								{
									result.putIfAbsent(currentWord, aliasCollection[0]);
								}
							}
						);
					});
		}

		return result;
	}
}
