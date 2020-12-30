package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.util.EmbedUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class PingCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		User author = ctx.getAuthor();
		MessageChannel channel = ctx.getChannel();
		JDA jda = ctx.getJDA();

		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}

		if(CooldownHandler.isOnCooldown(author.getId(), this))
		{
			return;
		}

		CooldownHandler.addCooldown(author.getId(), this);
		jda.getRestPing().queue(
				time -> channel.sendMessage(new EmbedBuilder()
						.setDescription("**Shard ID**: " + jda.getShardInfo().getShardId() + "\n**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms")
						.setColor(Constants.IGSQ_PURPLE)
						.setTimestamp(Instant.now())
						.build()).queue()
		);
	}

	@Override
	public String getName()
	{
		return "Ping";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("ping", "latency");
	}

	@Override
	public String getDescription()
	{
		return "Shows the bots current ping to Discord";
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
