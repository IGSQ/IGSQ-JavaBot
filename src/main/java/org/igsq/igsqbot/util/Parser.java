package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.entities.CommandContext;

import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser
{
	private static final Pattern ID_REGEX = Pattern.compile("(\\d{17,18})");
	private final String arg;
	private final CommandContext ctx;

	public Parser(String arg, CommandContext ctx)
	{
		this.arg = arg;
		this.ctx = ctx;
	}

	public OptionalInt parseAsUnsignedInt()
	{
		try
		{
			return OptionalInt.of(Integer.parseUnsignedInt(arg));
		}
		catch(NumberFormatException exception)
		{
			ctx.replyError("Entered value is either negative or not a number");
			return OptionalInt.empty();
		}
	}

	public void parseAsTextChannel(Consumer<TextChannel> consumer)
	{
		parseAsMentionable(mentionable -> consumer.accept((TextChannel) mentionable), Message.MentionType.CHANNEL);
	}

	public void parseAsRole(Consumer<Role> consumer)
	{
		parseAsMentionable(mentionable -> consumer.accept(((Role) mentionable)), Message.MentionType.ROLE);
	}

	public void parseAsUser(Consumer<User> consumer)
	{
		parseAsMentionable(mentionable -> consumer.accept((User) mentionable), Message.MentionType.USER);
	}

	private void parseAsMentionable(Consumer<IMentionable> consumer, Message.MentionType type)
	{
		Message message = ctx.getMessage();
		Guild guild = ctx.getGuild();
		User author = ctx.getAuthor();
		String typeName = type.name().toLowerCase();
		String notFound = typeName.toUpperCase() + " not found";
		Matcher idMatcher = ID_REGEX.matcher(arg);
		JDA jda = ctx.getJDA();
		SelfUser selfUser = jda.getSelfUser();

		if(type.getPattern().matcher(arg).matches()) //Direct mention
		{
			IMentionable mention = message.getMentions(type).get(0);
			consumer.accept(mention);
			return;
		}

		if(idMatcher.matches()) //ID
		{
			long mentionableId = Long.parseLong(idMatcher.group());
			if(type == Message.MentionType.USER)
			{
				if(mentionableId == author.getIdLong())
				{
					consumer.accept(author);
				}
				else if(mentionableId == selfUser.getIdLong())
				{
					consumer.accept(selfUser);
				}
				else
				{
					jda.retrieveUserById(mentionableId).queue(consumer, failure -> ctx.replyError("User not found."));
				}
			}
			else if(type == Message.MentionType.CHANNEL)
			{
				MessageChannel channel = jda.getTextChannelById(mentionableId);
				if(channel != null)
				{
					consumer.accept((IMentionable) channel);
				}
				else
				{
					ctx.replyError("Channel not found / i do not have permissions to see it.");
				}
			}
		}

		if(arg.length() >= 2 && arg.length() <= 32) //Named users
		{
			if(type == Message.MentionType.USER)
			{
				if(arg.equalsIgnoreCase(ctx.getMember().getEffectiveName()))
				{
					consumer.accept(author);
					return;
				}
				message.getGuild().retrieveMembersByPrefix(arg, 10)
						.onSuccess(members ->
						{
							if(members.isEmpty())
							{
								ctx.replyError("User not found.");
							}
							else
							{
								consumer.accept(members.get(0).getUser());
							}
						});
				return;
			}
			var rolesChannelsList = type == Message.MentionType.CHANNEL ? guild.getTextChannelsByName(arg, true) : guild.getRolesByName(arg, true);
			if(rolesChannelsList.isEmpty()) //Role / Channel
			{
				ctx.replyError("No " + typeName.toLowerCase() + "s with that name found");
			}
			else
			{
				consumer.accept(rolesChannelsList.get(0));
			}
		}
		ctx.replyError(notFound);
	}
}
