package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;

import java.util.List;

public class InfoCommand extends Command
{
	public InfoCommand()
	{
		super("Info", new String[]{"info"}, "Shows various amounts of information.", "[guild|user|role]", new Permission[]{}, true, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		//TODO: Implement this
	}
}
