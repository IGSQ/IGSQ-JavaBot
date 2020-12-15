package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.MessageCache;
import org.igsq.igsqbot.util.EmbedUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Clear_Command extends Command
{
	public Clear_Command()
	{
		super("clear", new String[]{"purge"}, "Clears the channel with the specified amount", "[amount -50-]", new Permission[]{Permission.MESSAGE_MANAGE}, true,5);
	}

	@Override
	public void execute(List<String> args, Context ctx)
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
			EmbedUtils.sendSyntaxError(channel,this);
			return;
		}
		if(amount <= 0)
		{
			EmbedUtils.sendSyntaxError(channel,this);
		}
		else if(amount > 51)
		{
			EmbedUtils.sendSyntaxError(channel,this);
		}
		else if(CooldownHandler.isOnCooldown(author.getIdLong(), this))
		{
			return;
		}

		CooldownHandler.addCooldown(author.getIdLong(), this);
		channel.getHistory().retrievePast(amount).queue(
				messages ->
				{
					channel.purgeMessages(messages);
					new EmbedGenerator(channel)
							.text("Deleted " + (messages.size()) + " messages")
							.color(Color.GREEN)
							.sendTemporary(5000);

					final MessageCache cache;

					if(!MessageCache.isGuildCached(guild.getId()))
					{
						cache = MessageCache.addAndReturnCache(guild.getId());
					}
					else
					{
						cache = MessageCache.getCache(guild.getId());
					}

					List<String> messageIds = new ArrayList<>();
					messages.forEach(message -> messageIds.add(message.getId()));

					for(String selectedMessageID : messageIds)
					{
						if(cache.isInCache(selectedMessageID))
						{
							cache.remove(cache.get(selectedMessageID));
						}
					}
				}
		);

	}
}
