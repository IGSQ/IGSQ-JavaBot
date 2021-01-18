package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;

public class SyntaxException extends CommandException
{
	private final Command command;

	public SyntaxException(Command command)
	{
		super(command);
		this.command = command;
	}

	public SyntaxException(CommandContext ctx)
	{
		super(ctx.getCommand());
		this.command = ctx.getCommand();
	}

	public Command getCommand()
	{
		return command;
	}
}
