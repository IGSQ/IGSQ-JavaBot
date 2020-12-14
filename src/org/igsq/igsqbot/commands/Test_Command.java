package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;

import java.util.InputMismatchException;

public class Test_Command extends Command
{
	public Test_Command()
	{
		super("test", new String[]{}, "Placeholder class for command testing", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		new ErrorHandler(new InputMismatchException("THIS IS AN INPUT MISMATCH EXCEPTION"));
	}
}
