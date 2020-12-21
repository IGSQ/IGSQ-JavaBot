package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.FileUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class MockCommand extends Command
{
	public MockCommand()
	{
		super("Mock", new String[]{"mock"}, "Mocks the specified text", "[text]", new Permission[]{}, false, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();

		if(args.isEmpty() || CommandUtils.isArgsEmbedCompatible(args))
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			EmbedGenerator embed = new EmbedGenerator(channel)
					.title(mockText(args))
					.color(EmbedUtils.IGSQ_PURPLE)
					.image("attachment://mock.jpg");

			InputStream file = FileUtils.getResourceFile("mock.jpg", true);
			if(file != null)
			{
				channel.sendFile(file, "mock.jpg").embed(embed.getBuilder().build()).queue();
			}
			else
			{
				EmbedUtils.sendError(channel, "An error occurred while loading the mock image.");
			}

		}
	}

	private String mockText(List<String> args)
	{
		StringBuilder mockText = new StringBuilder();
		Random random = new Random();
		mockText.append('"');
		args.forEach(word ->
		{
			for(String selectedChar : word.split(""))
			{
				mockText.append(random.nextBoolean() ? selectedChar.toUpperCase() : selectedChar.toLowerCase());
			}
			mockText.append(" ");
		});
		mockText.deleteCharAt(mockText.lastIndexOf(" "));
		mockText.append('"');
		return mockText.toString();
	}
}
