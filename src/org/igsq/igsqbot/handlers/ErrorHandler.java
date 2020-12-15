package org.igsq.igsqbot.handlers;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.YamlUtils;

import java.awt.*;

public class ErrorHandler
{
    private final Exception exception;

    public ErrorHandler(Exception exception)
    {
        this.exception = exception;

        reportError();
    }

    private void reportError()
    {
        if(YamlUtils.isFieldEmpty("bot.error", "config"))
        {
            exception.printStackTrace();
        }
        else
        {
            GuildChannel errorChannel = IGSQBot.getJDA().getGuildChannelById(Yaml.getFieldString("bot.error", "config"));
            if(errorChannel == null)
            {
                exception.printStackTrace();
                return;
            }
            EmbedGenerator embed = new EmbedGenerator((MessageChannel) errorChannel)
                    .color(Color.RED)
                    .title("Unhandled Exception");

            StringBuilder  embedText = new StringBuilder();

            embedText.append("**").append(exception.toString()).append("**");
            embedText.append("```");
            for(StackTraceElement selectedElement: exception.getStackTrace())
            {
                embedText.append(selectedElement.toString()).append("\n\n");
            }
            embedText.append("```");
            embed.text(embedText.toString()).send();
        }
    }
}
