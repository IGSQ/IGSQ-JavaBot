package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.ErrorHandler;

import java.awt.Color;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Ping_Command
{
	private final User author;
	private final MessageChannel channel;
	private final JDA jda;
	private int averagePing;

	public Ping_Command(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.jda = event.getJDA();
		pingQuery();
	}

	private void pingQuery()
	{
		if(!author.isBot()) ping();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}

	private void ping()
	{
		EmbedGenerator embed = new EmbedGenerator(channel).color(Color.ORANGE).title("Pong!").footer(Common.getTimestamp());

		jda.getRestPing().queue(
			time ->
			{
				final String embedText = "**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms";
				embed.text(embedText);
				channel.sendMessage(embed.getBuilder().build()).queue();
			}
		);
	}
}
