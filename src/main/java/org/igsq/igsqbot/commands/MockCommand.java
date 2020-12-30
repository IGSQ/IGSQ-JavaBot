package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.FileUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MockCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();

		if(args.isEmpty() || CommandUtils.isArgsEmbedCompatible(args))
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			InputStream file = FileUtils.getResourceFile("mock.jpg");
			if(file != null)
			{
				channel.sendFile(file, "mock.jpg").embed(new EmbedBuilder()
						.setTitle(mockText(args))
						.setColor(Constants.IGSQ_PURPLE)
						.setImage("attachment://mock.jpg")
						.build()).queue();
			}
			else
			{
				EmbedUtils.sendError(channel, "An error occurred while loading the mock image.");
			}
		}
	}

	@Override
	public String getName()
	{
		return "Mock";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("mock");
	}

	@Override
	public String getDescription()
	{
		return "Mocks the specified text";
	}

	@Override
	public String getSyntax()
	{
		return "[text]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
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
