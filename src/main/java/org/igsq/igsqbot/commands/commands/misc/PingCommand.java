package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;

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
		JDA jda = ctx.getJDA();
		jda.getRestPing().queue(
				ping -> ctx.sendMessage(new EmbedBuilder()
						.setTitle("P" + "o".repeat((int) (ping / 100)) + "ng.")
						.setDescription("**Shard ID**: " + jda.getShardInfo().getShardId()
								+ "\n**REST Ping**: " + ping
								+ "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms")
						.setColor(Constants.IGSQ_PURPLE)));
	}
}
