package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;

public class CooldownException extends CommandException
{
	public CooldownException(Command command)
	{
		super(command);
	}
}
