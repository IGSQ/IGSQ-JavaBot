package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;

public class CooldownException extends CommandException
{
	/**
	 * The command was on cooldown.
	 */
	public CooldownException(Command command)
	{
		super(command);
	}
}
