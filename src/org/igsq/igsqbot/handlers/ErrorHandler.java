package org.igsq.igsqbot.handlers;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.Yaml_Utils;

import java.awt.*;
import java.util.InputMismatchException;

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
        GuildChannel errorChannel = Common.getJda().getGuildChannelById(!Yaml_Utils.isFieldEmpty("BOT.error", "config") ? Yaml.getFieldString("BOT.error", "config") : "1");
        if(errorChannel == null)
        {
            exception.printStackTrace();
        }
        else
        {
            EmbedGenerator embed = new EmbedGenerator((MessageChannel) errorChannel)
                    .color(Color.RED)
                    .title("Unhandled Exception: " + exception.toString());
            StringBuilder  embedText = new StringBuilder();

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
