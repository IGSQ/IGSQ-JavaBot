package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.objects.*;
import org.igsq.igsqbot.util.APIUtils;

public class Test_Command extends Command
{
	public Test_Command()
	{
		super("test", new String[]{}, "Placeholder class for command testing", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		ctx.getChannel().sendMessage(APIUtils.sendPOST("https://www.reddit.com/api/v1/access_token",
				"{\"grant_type\":\"client_credentials\"}")).queue();
	}
}
