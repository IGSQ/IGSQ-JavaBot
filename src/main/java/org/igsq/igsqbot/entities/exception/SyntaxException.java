package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;


public class SyntaxException extends CommandException
{
	/**
	 * The user entered invalid syntax.
	 */
	public SyntaxException(Command command)
	{
		super(command);
	}

	/**
	 * The user entered invalid syntax.
	 */
	public SyntaxException(CommandContext ctx)
	{
		super(ctx.getCommand());
	}
}
