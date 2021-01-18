package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;

public class IllegalLengthException extends SyntaxException
{
	public IllegalLengthException(Command command)
	{
		super(command);
	}

	public IllegalLengthException(CommandContext ctx)
	{
		super(ctx.getCommand());
	}
}
