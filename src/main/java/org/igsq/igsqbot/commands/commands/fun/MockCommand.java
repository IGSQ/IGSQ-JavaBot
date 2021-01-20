package org.igsq.igsqbot.commands.commands.fun;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.FileUtils;

@SuppressWarnings("unused")
public class MockCommand extends Command
{
	public MockCommand()
	{
		super("Mock", "Mocks the specified text.", "[text]");
		addAliases("mock");
	}

	@Override
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(ctx, failure)) return;
		if(CommandChecks.argsEmbedCompatible(ctx, failure)) return;

		InputStream file = FileUtils.getResourceFile("mock.jpg");
		if(file != null)
		{
			ctx.getChannel().sendFile(file, "mock.jpg").embed(new EmbedBuilder()
					.setTitle(mockText(args))
					.setColor(Constants.IGSQ_PURPLE)
					.setImage("attachment://mock.jpg")
					.setTimestamp(Instant.now())
					.build()).queue();
		}
		else
		{
			failure.accept(new CommandResultException("An error occurred while loading the mock image."));
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
