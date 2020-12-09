package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.ErrorHandler;

import java.awt.Color;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Ping_Command
{
	private final User author;
	private final MessageChannel channel;
	private final JDA jda;
	private final Cooldown_Handler cooldownHandler;

	public Ping_Command(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.jda = event.getJDA();
		this.cooldownHandler = new Cooldown_Handler(event.getGuild().getId());
		pingQuery();
	}

	private void pingQuery()
	{
		if(!author.isBot()) ping();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}

	private void ping()
	{
		if(cooldownHandler.isCooldownActive("ping"))
		{
			new EmbedGenerator(channel).color(Color.RED).text("This command is on cooldown. " + cooldownHandler.getCooldown("ping") / 1000 + " seconds left").sendTemporary();
			return;
		}
		cooldownHandler.createCooldown("ping", 10000);
		EmbedGenerator embed = new EmbedGenerator(channel).color(Color.ORANGE).title("Pong!").footer(Common.getTimestamp());

		jda.getRestPing().queue(
			time ->
			{
				final String embedText = "**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms";
				embed.text(embedText);
				channel.sendMessage(embed.getBuilder().build()).queue(message -> new EmbedGenerator(null).text(embedText + "\n\n**30Sec REST Average**: " + getAverageREST() + "ms").color(Color.ORANGE).title("Pong!").footer(Common.getTimestamp()).replace(message));
			}
		);
	}

	private String getAverageREST()
	{
		AtomicLong averagePing = new AtomicLong();

		ScheduledFuture<?> collectAverages = Common.scheduler.scheduleAtFixedRate(() -> jda.getRestPing().queueAfter(5000, TimeUnit.MILLISECONDS, averagePing::addAndGet), 0, 5000, TimeUnit.MILLISECONDS);
		ScheduledFuture<?> stopCollection = Common.scheduler.schedule(() -> collectAverages.cancel(false), 30000, TimeUnit.MILLISECONDS);

		try
		{
			while(!stopCollection.isDone())
			{
				synchronized(this)
				{
					wait(100);
				}
			}
			return "" + averagePing.get() / (30 / 5);
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			return "An error occurred while collecting the averages.";
		}
	}
}
