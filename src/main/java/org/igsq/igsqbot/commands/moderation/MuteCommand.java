package org.igsq.igsqbot.commands.moderation;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

import java.util.Collections;
import java.util.List;

public class MuteCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		//TO BE IMPLEMENTED
	}

	@Override
	public String getName()
	{
		return "Mute";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("mute");
	}

	@Override
	public String getDescription()
	{
		return "Mutes the specified user.";
	}

	@Override
	public String getSyntax()
	{
		return "[user][duration]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.hasPermission(Collections.singletonList(Permission.MESSAGE_MANAGE));
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 60;
	}
}
