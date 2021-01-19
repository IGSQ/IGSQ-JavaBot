package org.igsq.igsqbot.commands.commands.fun;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

@SuppressWarnings("unused")
public class PollCommand extends Command
{
	public PollCommand()
	{
		super("Poll", "Starts a poll for users to vote in.", "[title]/[option1]/[option2]{20}");
		addAliases("poll");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsSizeMatches(ctx, 1);

		StringBuilder options = new StringBuilder();
		MessageChannel channel = ctx.getChannel();
		User author = ctx.getAuthor();
		List<String> reactions = new ArrayList<>();
		List<String> slashArgs = new Parser(args.get(0), ctx).parseAsSlashArgs();

		CommandChecks.argsSizeSubceeds(slashArgs, ctx, 3);
		String topic = slashArgs.get(0);

		List<Emoji> emojis = Emoji.getPoll();
		for(int i = 1; i < slashArgs.size() && i < EmbedUtils.REACTION_LIMIT + 1; i++)
		{
			options.append(slashArgs.get(i)).append(" ").append(emojis.get(i - 1).getAsMessageable()).append("\n\n");
			reactions.add(emojis.get(i - 1).getUnicode());
		}

		channel.sendMessage(new EmbedBuilder()
				.setTitle("Poll:")
				.setDescription(topic)
				.addField("Options:", options.toString(), false)
				.setThumbnail(author.getEffectiveAvatarUrl())
				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue(message -> reactions.forEach(reaction -> message.addReaction(reaction).queue()));
	}
}
