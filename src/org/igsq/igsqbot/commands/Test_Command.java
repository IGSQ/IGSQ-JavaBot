package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.objects.*;

public class Test_Command extends Command
{
	public Test_Command()
	{
		super("test", new String[]{}, "Placeholder class for command testing", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		GUIGenerator generator = new GUIGenerator(new EmbedGenerator(ctx.getChannel()).text("REACT TO ME"));
		generator.getEmbed().text("" + generator.menu(ctx.getAuthor(), 10000, 10)).replace(generator.getMessage());
	}
}
