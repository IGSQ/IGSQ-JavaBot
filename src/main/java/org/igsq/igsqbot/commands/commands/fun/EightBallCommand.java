package org.igsq.igsqbot.commands.commands.fun;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.exception.CommandException;

@SuppressWarnings("unused")
public class EightBallCommand extends Command
{
	public EightBallCommand()
	{
		super("8ball", "Query the 8Ball.", "[question]");
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		Random random = new Random();
		List<String> options = new ArrayList<>(List.of(""));
		cmd.getChannel().sendMessage(options.get(random.nextInt(args.size()))).queue();
	}
}

