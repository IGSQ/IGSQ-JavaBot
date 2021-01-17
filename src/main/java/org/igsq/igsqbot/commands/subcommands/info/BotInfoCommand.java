package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.info.BotInfo;

public class BotInfoCommand extends Command
{
	public BotInfoCommand(Command parent)
	{
		super(parent, "bot", "Shows informaton about the bot", "[none]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("IGSQBot information")
				.addField("JVM Version", BotInfo.getJavaVersion(), true)
				.addField("Java Vendor", BotInfo.getJavaVendor(), true)
				.addBlankField(true)
				.addField("Thread Count", String.valueOf(BotInfo.getThreadCount()), true)
				.addField("Memory Usage", BotInfo.getMemoryFormatted(), true)
				.addBlankField(true)
				.addField("Shard info", ctx.getJDA().getShardInfo().getShardString(), true)
				.addField("Server count", String.valueOf(BotInfo.getTotalServers(ctx.getJDA().getShardManager())), true)
				.addBlankField(true)

				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue();
	}
}
