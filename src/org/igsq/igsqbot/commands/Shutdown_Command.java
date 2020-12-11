package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.Yaml;

public class Shutdown_Command extends Command
{
	public Shutdown_Command()
	{
		super("shutdown", new String[]{}, "Shuts the bot down using the proper methods", new Permission[]{Permission.ADMINISTRATOR}, false, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		ctx.getJDA().shutdown();
		Yaml.saveFileChanges("@all");
		Yaml.disregardAndCloseFile("@all");
		System.exit(0);
	}
}
