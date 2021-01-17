package org.igsq.igsqbot.commands.commands.fun;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.Emoji;
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
		StringBuilder options = new StringBuilder();
		MessageChannel channel = ctx.getChannel();
		User author = ctx.getAuthor();
		List<String> reactions = new ArrayList<>();

		if(args.size() != 1)
		{
			EmbedUtils.sendSyntaxError(ctx);
			return;
		}
		List<String> slashArgs = new Parser(args.get(0), ctx).parseAsSlashArgs();
		if(slashArgs.size() < 3)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
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
}
