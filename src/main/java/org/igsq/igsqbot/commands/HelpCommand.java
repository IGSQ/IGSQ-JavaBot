package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.cache.MessageDataCache;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.*;

public class HelpCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final EmbedBuilder embed = ctx.getIGSQBot().getHelpPages().get(0);
		final User author = ctx.getAuthor();
		final JDA jda = ctx.getJDA();


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
								final MessageDataCache messageDataCache = new MessageDataCache(message.getId(), jda);
								final Map<String, String> users = new HashMap<>();

								users.put("user", author.getId());
								messageDataCache.setType(MessageDataCache.MessageType.HELP);
								messageDataCache.setUsers(users);
								messageDataCache.setPage(1);
								messageDataCache.build();

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
