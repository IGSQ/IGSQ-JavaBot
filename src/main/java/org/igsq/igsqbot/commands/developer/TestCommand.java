package org.igsq.igsqbot.commands.developer;

import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

import java.util.Collections;
import java.util.List;

public class TestCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		//TEST METHOD
	}

	@Override
	public String getName()
	{
		return "test";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("test");
	}

	@Override
	public String getDescription()
	{
		return "Placeholder class for command testing";
	}

	@Override
	public String getSyntax()
	{
		return "[none]";
	}
	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.isDeveloper();
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
