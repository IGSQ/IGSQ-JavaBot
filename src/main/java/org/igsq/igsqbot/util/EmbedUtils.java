package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.CommandContext;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class EmbedUtils
{
	public static final int CHARACTER_LIMIT = 2048;
	public static final int REACTION_LIMIT = 20;
	public static final int EMBED_TITLE_LIMIT = 256;

	private EmbedUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void sendError(MessageChannel channel, String errorText)
	{
		sendDeletingEmbed(channel, new EmbedBuilder()
				.setDescription(Constants.FAILURE + errorText)
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendSyntaxError(CommandContext ctx)
	{
		sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
				.setDescription(Constants.FAILURE + "The provided syntax was incorrect.\n`" + ctx.getCommand().getAliases().get(0) + " " + ctx.getCommand().getSyntax() + "`")
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendPermissionError(CommandContext ctx)
	{
		sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
				.setDescription(Constants.FAILURE + " A permission error occurred when attempting to execute command:`" + ctx.getCommand().getAliases().get(0) + "`")
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendSuccess(MessageChannel channel, String successText)
	{
		sendDeletingEmbed(channel, new EmbedBuilder()
				.setDescription(Constants.SUCCESS + successText)
				.setColor(Color.GREEN)
				.setTimestamp(Instant.now()));
	}

	public static void sendDisabledError(CommandContext ctx)
	{
		sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
				.setDescription(Constants.FAILURE + " `" + ctx.getCommand().getName() + "` is currently disabled!")
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendExecutionError(CommandContext ctx)
	{
		sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
				.setDescription(Constants.FAILURE + " `" + ctx.getCommand().getName() + "` could not be executed.")
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendDeletingEmbed(MessageChannel channel, EmbedBuilder embed, long delay)
	{
		channel.sendMessage(embed.build()).queue(message -> message.delete().queueAfter(delay, TimeUnit.MILLISECONDS, null, error -> { }));
	}

	public static void sendDeletingEmbed(MessageChannel channel, EmbedBuilder embed)
	{
		sendDeletingEmbed(channel, embed, 10000);
	}

	public static void sendReplacedEmbed(Message message, EmbedBuilder newEmbed)
	{
		message.editMessage(newEmbed.build()).queue();
	}
}
