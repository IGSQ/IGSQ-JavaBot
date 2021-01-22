package org.igsq.igsqbot.commands.commands.fun;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;

@SuppressWarnings("unused")
public class CoinflipCommand extends Command
{
	public CoinflipCommand()
	{
		super("Coinflip", "Flips a coin.", "[none]");
		addAliases("coinflip");
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		Random random = new Random();
		String result;
		int chance = random.nextInt(2);

		if(chance == 1)
		{
			result = "Heads";
		}
		else
		{
			result = "Tails";
		}

		cmd.getChannel().sendMessage(result).queue();
	}
}

