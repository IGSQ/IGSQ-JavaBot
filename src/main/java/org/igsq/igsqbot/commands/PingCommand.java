package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PingCommand extends Command
{
	public PingCommand()
	{
		super("Ping", new String[]{"ping", "latency"}, "Shows the bots current ping to Discord", "[none]", new Permission[]{Permission.MESSAGE_MANAGE}, false, 10);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final User author = ctx.getAuthor();
		final MessageChannel channel = ctx.getChannel();
		final JDA jda = ctx.getJDA();

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
		EmbedGenerator embed = new EmbedGenerator(channel)
				.color(EmbedUtils.IGSQ_PURPLE)
				.title("Pong!")
				.footer(StringUtils.getTimestamp());

		jda.getRestPing().queue(
				time ->
				{
					embed.text("**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms");
					channel.sendMessage(embed.getBuilder().build()).queue(
							message ->
									new EmbedGenerator().text(
											"**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms"
													+ "\n\n**REST Average**: " + getAverageREST(jda) + "ms")

											.color(EmbedUtils.IGSQ_PURPLE)
											.title("Pong!")
											.footer(StringUtils.getTimestamp())
											.replace(message));
				}
		);
	}

	private String getAverageREST(JDA jda)
	{
		AtomicLong averagePing = new AtomicLong();

		ScheduledFuture<?> retrieveTask = TaskHandler.addRepeatingTask(() -> jda.getRestPing().queueAfter(5, TimeUnit.SECONDS, averagePing::addAndGet), TimeUnit.SECONDS, 30);
		ScheduledFuture<?> stopTask = TaskHandler.addTask(() -> retrieveTask.cancel(false), TimeUnit.SECONDS, 30);

		try
		{
			while(!stopTask.isDone())
			{
				synchronized(this)
				{
					wait(100);
				}
			}
			return "" + averagePing.get();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			return "An error occurred while collecting the averages.";
		}
	}
}
