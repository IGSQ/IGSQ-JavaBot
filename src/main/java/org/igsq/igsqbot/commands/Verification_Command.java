package main.java.org.igsq.igsqbot.commands;

import main.java.org.igsq.igsqbot.Common;
import main.java.org.igsq.igsqbot.Yaml;
import main.java.org.igsq.igsqbot.objects.Command;
import main.java.org.igsq.igsqbot.objects.Context;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.objects.EventWaiter;
import main.java.org.igsq.igsqbot.util.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.*;

public class Verification_Command extends Command
{
	private final List<String> messageContent = new ArrayList<>();

	public Verification_Command()
	{
		super("verify", new String[]{"v", "accept"}, "Verifies the specified user into the server","[user]", new Permission[]{}, true, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		final StringBuilder embedText = new StringBuilder();
		final MessageChannel channel = ctx.getChannel();
		final Guild guild = ctx.getGuild();
		final User verificationTarget;
		final User author = ctx.getAuthor();
		final JDA jda = ctx.getJDA();

		try
		{
			verificationTarget = UserUtils.getUserFromMention(args.get(0));
		}
		catch(Exception exception)
		{
			EmbedUtils.sendSyntaxError(channel,this);
			return;
		}

		if(args.size() != 1)
		{
			EmbedUtils.sendSyntaxError(channel,this);
		}

		channel.getHistory().retrievePast(10).queue(
			messages ->
			{
				messages.forEach(
				selectedMessage ->
				{
					if(selectedMessage.getAuthor().equals(verificationTarget) && !selectedMessage.getContentRaw().startsWith(Common.DEFAULT_BOT_PREFIX))
					{
						messageContent.addAll(Arrays.asList(selectedMessage.getContentRaw().split(" ")));
					}
				});


				final Map<String, String> aliasMatches = findMatches(CommandUtils.getAliases(guild.getId()));
				final Map<String, String> declineMatches = findMatches(CommandUtils.getDeclined(guild.getId()));

				aliasMatches.forEach((key, value) ->
				{
					if(declineMatches.containsKey(key))
					{
						aliasMatches.remove(key, value);
					}
				});

				aliasMatches.forEach((alias, role) ->
						embedText.append("Detected Role: ")
								.append(UserUtils.getRoleAsMention(role))
								.append(" (Sure)")
								.append("\n")
				);

				final Map<String, String> similarMatches = findSimilar(CommandUtils.getAliases(guild.getId()), aliasMatches.values().toArray(new String[0]));
				similarMatches.forEach((alias, role) ->
						embedText.append("Detected Role: ")
						.append(UserUtils.getRoleAsMention(role))
						.append(" (Guess) for '")
						.append(alias)
						.append("'\n"));

				new EmbedGenerator(channel)
						.title("Verification for " + verificationTarget.getAsTag())
						.text(embedText.length() == 0 ? "No roles found for this user." : embedText.toString())
						.color(EmbedUtils.IGSQ_PURPLE)
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
					long verificationMember = UserUtils.getMemberFromUser(verificationTarget, guild).getIdLong();

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
							EmbedUtils.sendError(channel, "There is no welcome channel setup, defaulting to role only verification.");
						}
						else
						{
							new EmbedGenerator(welcomeChannel)
									.title(verificationTarget.getAsTag())
									.text(verificationTarget.getAsMention() + " has joined the " + guild.getName())
									.color(EmbedUtils.IGSQ_PURPLE)
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
							EmbedUtils.sendError(channel, "There is no welcome channel setup, defaulting to role only verification.");
						}
						else
						{
							new EmbedGenerator(welcomeChannel)
									.title(verificationTarget.getAsTag())
									.text(verificationTarget.getAsMention() + " has joined the " + guild.getName())
									.color(EmbedUtils.IGSQ_PURPLE)
									.element("Roles", embedText.length() > 0 ? roleText.toString() : "No roles.");
						}
					}
				}
			}
		);
	}

	private Map<String, String> findMatches(Map<String, String> input)
	{
		Map<String, String> result = new HashMap<>();

		input.forEach((role, alias) ->
		{
			if(!result.containsValue(alias) && messageContent.contains(alias))
			{
				result.putIfAbsent(alias, role);
				messageContent.remove(alias);
			}
		});
		return result;
	}

	private Map<String, String> findSimilar(Map<String, String> aliases, String[] matchedRoles)
	{
		Map<String, String> result = new HashMap<>();


		aliases.forEach((alias, role) ->
		{
			if(!messageContent.contains(alias))
			{
				messageContent.forEach(currentWord ->
				{
					if(!ArrayUtils.isValueInArray(matchedRoles, role) && StringUtils.isOption(alias,currentWord,10))
					{
						result.putIfAbsent(currentWord, role);
					}
				});
			}
		});
		return result;
	}
}
