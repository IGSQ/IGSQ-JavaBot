package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;

import java.util.List;

public class Test_Command extends Command
{
	public Test_Command()
	{
		super("test", new String[]{}, "Placeholder class for command testing","[none]", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		new ErrorHandler(new InterruptedException("TEST EXCEPTION"));
	}
}
