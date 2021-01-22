package org.igsq.igsqbot.commands.commands.misc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;

@SuppressWarnings("unused")
public class UptimeCommand extends Command
{
	public UptimeCommand()
	{
		super("Uptime", "Displays the bots uptime.", "[none]");
		addAliases("uptime");
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		Duration uptime = Duration.between(cmd.getIGSQBot().getStartTimestamp(), LocalDateTime.now());
		cmd.sendMessage(new EmbedBuilder()
				.setDescription(
						"Uptime: " + uptime.toDaysPart() +
								" days, " + uptime.toHoursPart() +
								" hours, " + uptime.toSecondsPart() +
								" seconds.")
				.setColor(Constants.IGSQ_PURPLE));
	}
}
