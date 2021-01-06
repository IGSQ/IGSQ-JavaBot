package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Arrays;
import java.util.List;

public class HelpCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();
		EmbedBuilder embed = ctx.getIGSQBot().getHelpPages().get(0);

		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			channel.sendMessage(embed.build()).queue
					(
							message ->
							{
								message.addReaction("U+25C0").queue();
								message.addReaction("U+25B6").queue();
								message.addReaction("U+274C").queue();
							}
					);
		}
	}

	@Override
	public String getName()
	{
		return "Help";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("help", "?", "howto", "commands");
	}

	@Override
	public String getDescription()
	{
		return "Shows the help menu for this bot";
	}

	@Override
	public String getSyntax()
	{
		return "[none]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
