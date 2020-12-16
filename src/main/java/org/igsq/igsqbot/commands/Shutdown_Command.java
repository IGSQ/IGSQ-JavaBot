package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;

public class Shutdown_Command extends Command
{
	public Shutdown_Command()
	{
		super("shutdown", new String[]{}, "Shuts the bot down using the proper methods","[none]", new Permission[]{Permission.ADMINISTRATOR}, false, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx.getChannel(),this);
			return;
		}
		ctx.getJDA().shutdown();
		Yaml.saveFileChanges("@all");
		Yaml.disregardAndCloseFile("@all");
		System.exit(0);
	}
}
