package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

import java.util.List;

public class TestCommand extends Command
{
	public TestCommand()
	{
		super("test", new String[]{"test"}, "Placeholder class for command testing", "[none]", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		//PLACEHOLDER
	}
}
