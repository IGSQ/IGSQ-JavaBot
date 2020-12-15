package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.io.File;
import java.io.InputStream;
import java.util.EmptyStackException;
import java.util.Random;

public class Mock_Command extends Command
{
	public Mock_Command()
	{
		super("mock", new String[]{}, "Mocks the specified text", "[text]", new Permission[]{}, false, 0);
	}
	@Override
	public void execute(String[] args, Context ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final String inputText = ArrayUtils.arrayCompile(args, " ");
		final StringBuilder mockText = new StringBuilder();
		final Random random = new Random();

		if(args.length < 1)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			mockText.append('"');
			for(String selectedChar : inputText.split("")) mockText.append(random.nextBoolean() ? selectedChar.toUpperCase() : selectedChar);
			mockText.append('"');

			EmbedGenerator embed = new EmbedGenerator(channel)
					.title(mockText.toString())
					.color(EmbedUtils.IGSQ_PURPLE)
					.image("attachment://mock.jpg");
			try
			{
				InputStream file = new File("IGSQBot/src/org/igsq/igsqbot/media/mock.jpg").toURI().toURL().openStream();

				channel.sendFile(file, "mock.jpg").embed(embed.getBuilder().build()).queue();
			}
			catch(Exception exception)
			{
				EmbedUtils.sendError(channel, "An error occurred while loading the image");
			}
		}
	}
}
