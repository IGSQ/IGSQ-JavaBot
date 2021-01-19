package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;

public class MemberPermissionException extends CommandException
{
	/**
	 * An error occurred with the members permissions.
	 */
	public MemberPermissionException(Command command)
	{
		super(command);
	}
}
