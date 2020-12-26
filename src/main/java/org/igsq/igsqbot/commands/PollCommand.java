package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PollCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final StringBuilder options = new StringBuilder();
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();
		final List<String> reactions = new ArrayList<>();

		if(args.size() != 1 || CommandUtils.isArgsEmbedCompatible(args))
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}

		final List<String> slashArgs = new ArrayList<>(Arrays.asList(args.get(0).split("/")));
		if(slashArgs.size() >= 3 && !CommandUtils.isArgsEmbedCompatible(args))
		{
			String topic = slashArgs.get(0);
			for(int i = 1; i < slashArgs.size() && i < EmbedUtils.REACTION_LIMIT + 1; i++)
			{
				options.append(slashArgs.get(i)).append(" ").append(CommandUtils.POLL_EMOJIS.get(i - 1)).append("\n\n");
				reactions.add(CommandUtils.POLL_EMOJIS_UNICODE.get(i - 1));
			}
			new EmbedGenerator(channel)
					.title("Poll:")
					.text(topic)
					.element("Options:", options.toString())
					.footer("Poll created by " + author.getAsTag())
					.thumbnail(author.getAvatarUrl())
					.color(Constants.IGSQ_PURPLE)
					.reaction(reactions.toArray(new String[0]))
					.send();
		}
		else
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
	}

	@Override
	public String getName()
	{
		return "Poll";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("poll");
	}

	@Override
	public String getDescription()
	{
		return "Starts a poll for users to vote in.";
	}

	@Override
	public String getSyntax()
	{
		return "[title]/[option1]/[option2]{20}";
	}

	@Override
	public List<Permission> getPermissions()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean isRequiresGuild()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
