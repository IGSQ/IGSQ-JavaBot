package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.OptionalInt;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.util.Parser;

@SuppressWarnings("unused")
public class HelpCommand extends Command
{
	public HelpCommand()
	{
		super("Help", "Shows the help menu for this bot.", "[page / command]");
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
			Command command = ctx.getIGSQBot().getCommandHandler().getCommandMap().get(args.get(0));
			if(command == null)
			{
				page = new Parser(args.get(0), ctx).parseAsUnsignedInt();
			}
			else
			{
				ctx.sendMessage(generateHelpPerCommand(command, ctx.getPrefix()));
				return;
			}
		}


		if(page.isPresent())
		{
			if(page.getAsInt() + 1 > ctx.getIGSQBot().getHelpPages().size() + 1)
			{
				throw new IllegalArgumentException("Page `" + args.get(0) + "` does not exist.");
			}

			ctx.sendMessage(ctx.getIGSQBot().getHelpPages().get(page.getAsInt() - 1));
		}
	}

	private EmbedBuilder generateHelpPerCommand(Command command, String prefix)
	{
		EmbedBuilder result = new EmbedBuilder()
				.setTitle("**Help for " + command.getName() + "**")
				.setFooter("<> Optional;  [] Required; {} Maximum Quantity | ");
		result.addField(prefix + command.getAliases().get(0),  command.getDescription() + "\n`Syntax: " + command.getSyntax() + "`", false);
		if(command.hasChildren())
		{
			command.getChildren().forEach(
			child ->
				result.addField(prefix + command.getAliases().get(0) + " " + child.getName(), child.getDescription() + "\n`Syntax: " + child.getSyntax() + "`", false));
		}
		return result;
	}
}
