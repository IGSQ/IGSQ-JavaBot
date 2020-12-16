package main.java.org.igsq.igsqbot.commands;

import main.java.org.igsq.igsqbot.handlers.ErrorHandler;
import main.java.org.igsq.igsqbot.objects.Command;
import main.java.org.igsq.igsqbot.objects.Context;
import net.dv8tion.jda.api.Permission;

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
