package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.EmbedGenerator;

import java.awt.*;

public class EmbedUtils
{
	public static final int CHARACTER_LIMIT = 2048;
	public static final int REACTION_LIMIT = 20;
	public static final int EMBED_TITLE_LIMIT = 256;
	public static final Color IGSQ_PURPLE = new Color(104, 89, 133);

	private EmbedUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void sendError(MessageChannel channel, String errorText)
	{
		new EmbedGenerator(channel)
				.text(":x: " + errorText)
				.color(Color.RED)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}

	public static void sendSyntaxError(MessageChannel channel, Command command)
	{
		new EmbedGenerator(channel)
				.text(":negative_squared_cross_mark: The provided syntax was incorrect.\n`" + Common.BOT_PREFIX + command.getInvoke() +  " " + command.getSyntax() + "`")
				.color(Color.RED)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}

	public static void sendPermissionError(MessageChannel channel, Command command)
	{
		new EmbedGenerator(channel)
				.text(":negative_squared_cross_mark: A permission error occurred when attempting to execute command:`" + Common.BOT_PREFIX + command.getInvoke() + "`")
				.color(Color.RED)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}

	public static void sendSuccessMessage(MessageChannel channel, String successText)
	{
		new EmbedGenerator(channel)
				.text(":white_check_mark: " + successText)
				.color(Color.GREEN)
				.footer(StringUtils.getTimestamp())
				.sendTemporary();
	}
}
