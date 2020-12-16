package main.java.org.igsq.igsqbot.commands;

import main.java.org.igsq.igsqbot.Common;
import main.java.org.igsq.igsqbot.objects.Command;
import main.java.org.igsq.igsqbot.objects.Context;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import main.java.org.igsq.igsqbot.util.StringUtils;
import net.dv8tion.jda.api.Permission;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Uptime_Command extends Command
{
	public Uptime_Command()
	{
		super("uptime", new String[]{}, "Displays the bots uptime", "[none]", new Permission[]{}, false, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx.getChannel(), this);
			return;
		}
		Duration uptime = Duration.between(Common.START_TIMESTAMP, LocalDateTime.now());
		new EmbedGenerator(ctx.getChannel())
				.text("Uptime: " + uptime.toDays() + " days, " + uptime.toHours() + " hours, " + uptime.toSeconds() + " seconds.")
				.color(EmbedUtils.IGSQ_PURPLE)
				.footer(StringUtils.getTimestamp())
				.send();
	}
}
