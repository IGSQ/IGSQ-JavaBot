package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;


public class IllegalLengthException extends SyntaxException
{
	/**
	 * The user's input was too large to be considered reasonable.
	 */
	public IllegalLengthException(Command command)
	{
		super(command);
	}

	/**
	 * The user's input was too large to be considered reasonable.
	 */
	public IllegalLengthException(CommandContext ctx)
	{
		super(ctx.getCommand());
	}
}
