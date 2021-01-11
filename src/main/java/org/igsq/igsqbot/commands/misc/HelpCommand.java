package org.igsq.igsqbot.commands.misc;

import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

public class HelpCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(args.size() != 1)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			OptionalInt page = new Parser(args.get(0), ctx).parseAsUnsignedInt();
			if(page.isPresent())
			{
				if(page.getAsInt() + 1 > ctx.getIGSQBot().getHelpPages().size() + 1)
				{
					ctx.replyError("That page does not exist");
				}
				else
				{
					ctx.getChannel().sendMessage(ctx.getIGSQBot().getHelpPages().get(page.getAsInt() - 1).build()).queue();
				}
			}
		}
	}

	@Override
	public String getName()
	{
		return "Help";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("help", "?", "howto", "commands");
	}

	@Override
	public String getDescription()
	{
		return "Shows the help menu for this bot";
	}

	@Override
	public String getSyntax()
	{
		return "[page]";
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
}
