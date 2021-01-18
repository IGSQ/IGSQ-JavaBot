package org.igsq.igsqbot.entities.exception;

import org.igsq.igsqbot.entities.command.Command;

public class MemberPermissionException extends CommandException
{
	public MemberPermissionException(Command command)
	{
		super(command);
	}
}
