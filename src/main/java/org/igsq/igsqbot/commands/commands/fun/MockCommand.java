package org.igsq.igsqbot.commands.commands.fun;

import java.io.InputStream;
import java.util.List;
import java.util.Random;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.FileUtils;

@SuppressWarnings("unused")
public class MockCommand extends Command
{
	public MockCommand()
	{
		super("Mock", "Mocks the specified text", "[text]");
		addAliases("mock");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();

		if(args.isEmpty() || CommandUtils.isArgsEmbedCompatible(args))
		{
			EmbedUtils.sendSyntaxError(ctx);
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
				ctx.replyError("An error occurred while loading the mock image.");
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
