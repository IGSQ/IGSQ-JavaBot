package org.igsq.igsqbot.improvedcommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.awt.Color;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Ping_Command extends Command
{
	public Ping_Command()
	{
		super("ping", new String[]{}, "Shows the bots current ping to Discord", new Permission[]{Permission.MESSAGE_MANAGE},false, 10);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final User author = ctx.getAuthor();
		final MessageChannel channel = ctx.getChannel();
		final JDA jda = ctx.getJDA();

		if(CooldownHandler.isOnCooldown(author.getIdLong(), this))
		{
			return;
		}
		CooldownHandler.addCooldown(author.getIdLong(), this);
		EmbedGenerator embed = new EmbedGenerator(channel).color(Color.ORANGE).title("Pong!").footer(Common.getTimestamp());

		jda.getRestPing().queue(
				time ->
				{
					embed.text("**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms");
					channel.sendMessage(embed.getBuilder().build()).queue(
							message ->
							{
								new EmbedGenerator().text(
										"**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms"
												+ "\n\n**30 Second REST Average**: " + getAverageREST(30, jda) + "ms"
												+ "\n**1 Minute REST Average**: " + getAverageREST(60, jda) + "ms")

										.color(Color.ORANGE)
										.title("Pong!")
										.footer(Common.getTimestamp())
										.replace(message);
							});
				}
		);
	}

	private String getAverageREST(int seconds, JDA jda)
	{
		AtomicLong averagePing = new AtomicLong();

		ScheduledFuture<?> collectAverages = Common.scheduler.scheduleAtFixedRate(() -> jda.getRestPing().queueAfter(5, TimeUnit.SECONDS, averagePing::addAndGet), 0, 5000, TimeUnit.MILLISECONDS);
		ScheduledFuture<?> stopCollection = Common.scheduler.schedule(() -> collectAverages.cancel(false), seconds, TimeUnit.SECONDS);

		try
		{
			while(!stopCollection.isDone())
			{
				synchronized(this)
				{
					wait(100);
				}
			}
			return "" + averagePing.get() / (seconds / 5);
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			return "An error occurred while collecting the averages.";
		}
	}
}
