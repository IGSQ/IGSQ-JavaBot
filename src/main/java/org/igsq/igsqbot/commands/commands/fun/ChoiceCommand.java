package org.igsq.igsqbot.commands.commands.fun;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.CommandChecks;

@SuppressWarnings("unused")
public class ChoiceCommand extends Command
{
	public ChoiceCommand()
	{
		super("choice", "Chooses a 50/50 for you.", "[opt1][opt2]");
		addAliases("choice");
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(cmd, 2, failure)) return;
		Random random = new Random();
		String choice;
		int chance = random.nextInt(2);

		if(chance == 1)
		{
			choice = args.get(0);
		}
		else
		{
			choice = args.get(1);
		}

		choice = Message.MentionType.ROLE.getPattern().matcher(choice).replaceAll("");
		choice = Message.MentionType.EVERYONE.getPattern().matcher(choice).replaceAll("");
		choice = Message.MentionType.HERE.getPattern().matcher(choice).replaceAll("");

		cmd.getChannel().sendMessage("I choose " + choice).queue();
	}
}

