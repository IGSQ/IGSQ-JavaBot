package org.igsq.igsqbot.util;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.Emoji;

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
				.setDescription(Emoji.FAILURE.getAsMessageable() + errorText)
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendSyntaxError(CommandContext ctx)
	{
		Command cmd = ctx.getCommand();
		if(ctx.isChild())
		{
			sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
					.setDescription(Emoji.FAILURE.getAsMessageable() + "The provided syntax was incorrect.\n`" + cmd.getParent().getAliases().get(0) + " " + cmd.getName() + " " + cmd.getSyntax() + "`")
					.setColor(Color.RED)
					.setTimestamp(Instant.now()));
		}
		else
		{
			sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
					.setDescription(Emoji.FAILURE.getAsMessageable() + "The provided syntax was incorrect.\n`" + cmd.getAliases().get(0) + " " + cmd.getSyntax() + "`")
					.setColor(Color.RED)
					.setTimestamp(Instant.now()));
		}
	}

	public static void sendPermissionError(CommandContext ctx)
	{
		Command cmd = ctx.getCommand();
		sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
				.setDescription(Emoji.FAILURE.getAsMessageable() + " You are missing the following permissions for command:`" + cmd.getAliases().get(0) + "`" +
						cmd.getRequiredPermissions().stream().map(Permission::getName).collect(Collectors.joining(" ")))
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendSuccess(MessageChannel channel, String successText)
	{
		sendDeletingEmbed(channel, new EmbedBuilder()
				.setDescription(Emoji.SUCCESS.getAsMessageable() + successText)
				.setColor(Color.GREEN)
				.setTimestamp(Instant.now()));
	}

	public static void sendDisabledError(CommandContext ctx)
	{
		sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
				.setDescription(Emoji.FAILURE.getAsMessageable() + " `" + ctx.getCommand().getName() + "` is currently disabled!")
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendDeletingEmbed(MessageChannel channel, EmbedBuilder embed, long delay)
	{
		channel.sendMessage(embed.build()).queue(message -> message.delete().queueAfter(delay, TimeUnit.MILLISECONDS, null, error ->
		{
		}));
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
