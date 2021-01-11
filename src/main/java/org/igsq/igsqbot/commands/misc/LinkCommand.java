package org.igsq.igsqbot.commands.misc;

import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

import java.util.Arrays;
import java.util.List;

public class LinkCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		//TO BE IMPLEMENTED
	}

	@Override
	public String getName()
	{
		return "Link";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("link", "mclink", "minecraft");
	}

	@Override
	public String getDescription()
	{
		return "Controls Minecraft links.";
	}

	@Override
	public String getSyntax()
	{
		return "[add|remove][mcName] | [list]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.getIGSQBot().getMinecraft().getDatabaseHandler().isOnline();
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

	
