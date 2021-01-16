package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;

public class BotInfoCommand extends Command
{
	public BotInfoCommand(Command parent)
	{
		super(parent, "bot", "Shows informaton about the bot", "[none]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		org.igsq.igsqbot.entities.info.BotInfo botInfo = new org.igsq.igsqbot.entities.info.BotInfo(ctx.getIGSQBot());

		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("IGSQBot information")
				.addField("JVM Version", botInfo.getJavaVersion(), true)
				.addField("Java Vendor", botInfo.getJavaVendor(), true)
				.addBlankField(true)
				.addField("Thread Count", String.valueOf(botInfo.getThreadCount()), true)
				.addField("Memory Usage", botInfo.getMemoryFormatted(), true)
				.addBlankField(true)

				.addField("Shard count", String.valueOf(botInfo.getTotalShards()), true)
				.addField("Server count", String.valueOf(botInfo.getTotalServers()), true)

				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue();
	}
}
