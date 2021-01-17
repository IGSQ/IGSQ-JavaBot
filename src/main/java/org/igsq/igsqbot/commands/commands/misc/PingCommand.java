package org.igsq.igsqbot.commands.commands.misc;

import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

@SuppressWarnings("unused")
public class PingCommand extends Command
{
	public PingCommand()
	{
		super("Ping", "Shows the bot's ping to Discord.", "[none]");
		addAliases("ping");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();
		JDA jda = ctx.getJDA();

		jda.getRestPing().queue(
				time -> channel.sendMessage(new EmbedBuilder()
						.setDescription("**Shard ID**: " + jda.getShardInfo().getShardId()
								+ "\n**REST Ping**: " + time
								+ "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms")
						.setColor(Constants.IGSQ_PURPLE)
						.setTimestamp(Instant.now())
						.build()).queue());
	}
}
