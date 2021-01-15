package org.igsq.igsqbot.commands.moderation;

import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

import java.util.Collections;
import java.util.List;

public class WarnCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{

	}


	@Override
	public String getName()
	{
		return "Warn";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("warn");
	}

	@Override
	public String getDescription()
	{
		return "Handles the user warning system";
	}

	@Override
	public String getSyntax()
	{
		return "[user][reason] | [show][user] | [remove][user][number]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public long getCooldown()
	{
		return 0;
	}
}
