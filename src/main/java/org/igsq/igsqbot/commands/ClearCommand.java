package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.util.EmbedUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ClearCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final int amount;
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();
		final Guild guild = ctx.getGuild();

		try
		{
			amount = Integer.parseInt(args.get(0));
		}
		catch(Exception exception)
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}
		if(amount <= 0)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(amount > 51)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(CooldownHandler.isOnCooldown(author.getId(), this))
		{
			return;
		}

		channel.getIterableHistory().takeAsync(amount).thenAccept(messages ->
		{
			channel.purgeMessages(messages);
			new EmbedGenerator(channel)
					.text("Deleted " + (messages.size()) + " messages")
					.color(Color.GREEN)
					.sendTemporary(5000);

			final MessageCache cache = MessageCache.getCache(guild);
			messages.stream().filter(cache::isInCache).forEach(cache::remove);
		});
	}

	@Override
	public String getName()
	{
		return "Clear";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("clear", "purge");
	}

	@Override
	public String getDescription()
	{
		return "Clears the channel with the specified amount";
	}

	@Override
	public String getSyntax()
	{
		return "[amount {50}]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.hasPermission(Collections.singletonList(Permission.MESSAGE_MANAGE));
	}

	@Override
	public boolean isRequiresGuild()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 10;
	}
}
