package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.EmbedGenerator;

import java.awt.*;

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
		new EmbedGenerator(channel)
				.text(Constants.FAILURE + errorText)
				.color(Color.RED)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}

	public static void sendSyntaxError(MessageChannel channel, Command command)
	{
		new EmbedGenerator(channel)
				.text(Constants.FAILURE + " The provided syntax was incorrect.\n`" + command.getAliases()[0] + " " + command.getSyntax() + "`")
				.color(Color.RED)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}

	public static void sendPermissionError(MessageChannel channel, Command command)
	{
		new EmbedGenerator(channel)
				.text(Constants.FAILURE + " A permission error occurred when attempting to execute command:`" + command.getAliases()[0] + "`")
				.color(Color.RED)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}

	public static void sendSuccess(MessageChannel channel, String successText)
	{
		new EmbedGenerator(channel)
				.text(Constants.SUCCESS + successText)
				.color(Color.GREEN)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}

	public static void sendDisabledError(MessageChannel channel, Command command)
	{
		new EmbedGenerator(channel)
				.text(Constants.FAILURE + " `" + command.getName() + "` is currently disabled!")
				.color(Color.RED)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}
}
