package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.handlers.TaskHandler;

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

	public static void sendSyntaxError(MessageChannel channel, Command command)
	{
		sendDeletingEmbed(channel, new EmbedBuilder()
				.setDescription(Constants.FAILURE + " The provided syntax was incorrect.\n`" + command.getAliases().get(0) + " " + command.getSyntax() + "`")
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendPermissionError(MessageChannel channel, Command command)
	{
		sendDeletingEmbed(channel, new EmbedBuilder()
				.setDescription(Constants.FAILURE + " A permission error occurred when attempting to execute command:`" + command.getAliases().get(0) + "`")
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

	public static void sendDisabledError(MessageChannel channel, Command command)
	{
		sendDeletingEmbed(channel, new EmbedBuilder()
				.setDescription(Constants.FAILURE + " `" + command.getName() + "` is currently disabled!")
				.setColor(Color.RED)
				.setTimestamp(Instant.now()));
	}

	public static void sendExecutionError(MessageChannel channel, Command command)
	{
		sendDeletingEmbed(channel, new EmbedBuilder()
				.setDescription(Constants.FAILURE + " `" + command.getName() + "` could not be executed, this could be due to a permission error.")
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

	public static void sendReplacedEmbed(Message message, EmbedBuilder newEmbed, long delay, boolean overwritePending)
	{
		if(!overwritePending && Yaml.getFieldBool(message.getId() + ".changepending", Filename.INTERNAL))
		{
			return;
		}
		if(message.getEmbeds().isEmpty())
		{
			MessageEmbed oldEmbed = message.getEmbeds().get(0);
			message.editMessage(newEmbed.build()).queue(editedMessage ->
			{
				editedMessage.editMessage(oldEmbed).queueAfter(delay, TimeUnit.MILLISECONDS);
				Yaml.updateField(message.getId() + ".changepending", Filename.INTERNAL, true);
				TaskHandler.addTask(() -> Yaml.updateField(message.getId() + ".changepending", Filename.INTERNAL, false), TimeUnit.MILLISECONDS, delay);
			});
		}
	}

	public static void sendReplacedEmbed(Message message, EmbedBuilder newEmbed, long delay)
	{
		sendReplacedEmbed(message, newEmbed, delay, false);
	}

	public static void sendReplacedEmbed(Message message, EmbedBuilder newEmbed, boolean overwritePending)
	{
		if(!overwritePending && Yaml.getFieldBool(message.getId() + ".changepending", Filename.INTERNAL))
		{
			return;
		}
		message.editMessage(newEmbed.build()).queue();
	}
}
