package org.igsq.igsqbot;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;

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
        GuildChannel errorChannel = Common.jda.getGuildChannelById(Yaml.getFieldString("BOT.error", "config"));
        if(errorChannel == null)
        {
            exception.printStackTrace();
        }
        else
        {
            EmbedGenerator embed =  new EmbedGenerator((MessageChannel) errorChannel).title("Exception by Class: " + exception.getCause().getClass()).color(Color.RED);
            if(exception.getMessage().length() <= 1024)
            {
                embed.text("Stacktrace: \n" + exception.getMessage());
            }
            else if(exception.getMessage().length() > 1024)
            {
                for(int i = 0; i < exception.getMessage().length(); i += 1024)
                {
                    embed.element("Stacktrace: ", exception.getMessage().substring(i));
                }
            }
            else
            {
                embed.text("Exception message was empty");
            }
            embed.send();
        }
    }

    public static void causeException()
    {
        new ErrorHandler(new InputMismatchException("THIS IS AN INPUT MISMATCH EXCEPTION"));
    }
}
