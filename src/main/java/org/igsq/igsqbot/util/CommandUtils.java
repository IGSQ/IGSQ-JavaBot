package org.igsq.igsqbot.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.GuildConfig;

public class CommandUtils
{
	public static final List<String> POLL_EMOJIS_UNICODE = List.of("U+1F350", " U+1F349", "U+1F34D", "U+1F34E", "U+1F34C", "U+1F951", "U+1F346", "U+1F95D", "U+1F347", "U+1FAD0", "U+1F352", "U+1F9C5", "U+1F351", "U+1F34B", "U+1F34A", "U+1F348", "U+1F965", "U+1F9C4", "U+1F952", "U+1F991");
	public static final List<String> POLL_EMOJIS = List.of(":pear:", ":watermelon:", ":pineapple:", ":apple:", ":banana:", ":avocado:", ":eggplant:", ":kiwi:", ":grapes:", ":blueberries:", ":cherries:", ":onion:", ":peach:", ":lemon:", ":tangerine:", ":melon:", ":coconut:", ":garlic:", ":cucumber:", ":squid:");

	private CommandUtils()
	{
		// Override the default, public, constructor
	}

	public static boolean isArgsEmbedCompatible(List<String> args)
	{
		return Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).count() > EmbedUtils.CHARACTER_LIMIT;
	}

	public static boolean isValidCommand(String message, long guildId, IGSQBot igsqBot)
	{
		return message.startsWith(new GuildConfig(guildId, igsqBot).getPrefix()) || message.startsWith("<@" + igsqBot.getSelfUser().getId() + ">") || message.startsWith("<@!" + igsqBot.getSelfUser().getId() + ">");
	}

	public static void interactionCheck(User user1, User user2, CommandContext ctx, Runnable onSuccess)
	{
		List<RestAction<?>> actions = new ArrayList<>();
		actions.add(ctx.getGuild().retrieveMember(user1));
		actions.add(ctx.getGuild().retrieveMember(user2));
		RestAction.allOf(actions).queue(results ->
				{
					Member member1 = (Member) results.get(0);
					Member member2 = (Member) results.get(1);

					if(member1.canInteract(member2))
					{
						onSuccess.run();
					}
					else
					{
						ctx.replyError("A hierarchy issue occurred when tried to execute command `" + ctx.getCommand().getName() + "`");
					}
				}
		);
	}

	public static ZoneOffset getLocalOffset()
	{
		return OffsetDateTime.now().getOffset();
	}
}
