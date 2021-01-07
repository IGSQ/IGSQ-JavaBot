package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class UptimeCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
			return;
		}

		final Duration uptime = Duration.between(ctx.getIGSQBot().getStartTimestamp(), LocalDateTime.now());
		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setDescription("Uptime: " + uptime.toDaysPart() + " days, " + uptime.toHoursPart() + " hours, " + uptime.toSecondsPart() + " seconds.")
				.setColor(Constants.IGSQ_PURPLE)
				.setTimestamp(Instant.now())
				.build()).queue();
	}

	@Override
	public String getName()
	{
		return "Uptime";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("uptime");
	}

	@Override
	public String getDescription()
	{
		return "Displays the bots uptime";
	}

	@Override
	public String getSyntax()
	{
		return "[none]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
