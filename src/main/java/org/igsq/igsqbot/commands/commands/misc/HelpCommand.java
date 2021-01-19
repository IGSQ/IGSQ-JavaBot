package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.OptionalInt;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.util.Parser;

@SuppressWarnings("unused")
public class HelpCommand extends Command
{
	public HelpCommand()
	{
		super("Help", "Shows the help menu for this bot.", "[page]");
		addAliases("help", "?", "howto", "commands");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		OptionalInt page;
		if(args.isEmpty())
		{
			page = OptionalInt.of(1);
		}
		else
		{
			page = new Parser(args.get(0), ctx).parseAsUnsignedInt();
		}

		if(page.isPresent())
		{
			if(page.getAsInt() + 1 > ctx.getIGSQBot().getHelpPages().size() + 1)
			{
				throw new IllegalArgumentException("That page does not exist");
			}

			ctx.sendMessage(ctx.getIGSQBot().getHelpPages().get(page.getAsInt() - 1));
		}
	}
}
