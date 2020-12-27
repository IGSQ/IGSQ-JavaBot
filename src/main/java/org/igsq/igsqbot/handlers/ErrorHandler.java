package org.igsq.igsqbot.handlers;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.yaml.BotConfig;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class ErrorHandler
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);
	private final Exception exception;

	public ErrorHandler(Exception exception)
	{
		this.exception = exception;

		reportError();
	}

	private void reportError()
	{
		if(YamlUtils.isFieldEmpty("bot.error", Filename.CONFIG))
		{
			LOGGER.error("An exception occurred.", exception);
		}
		else
		{
			MessageChannel errorChannel = IGSQBot.getShardManager().getTextChannelById(new BotConfig().getErrorChannel());
			if(errorChannel == null)
			{
				LOGGER.error("An exception occurred.", exception);
				return;
			}
			EmbedGenerator embed = new EmbedGenerator(errorChannel)
					.color(Color.RED)
					.title("Unhandled Exception");

			StringBuilder embedText = new StringBuilder();

			embedText.append("**").append(exception.toString()).append("**");
			embedText.append("```");
			for(StackTraceElement selectedElement : exception.getStackTrace())
			{
				embedText.append(selectedElement.toString()).append("\n\n");
			}
			embedText.append("```");
			embed.text(embedText.toString()).send();
		}
	}
}
