package org.igsq.igsqbot.commands.subcommands.warning;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;

public class WarningShowCommand extends Command
{
	public WarningShowCommand(Command parent)
	{
		super(parent, "show", "Shows a user's warnings", "[user]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
			return;
		}
		new Parser(args.get(0), ctx).parseAsUser(user ->
		{
			Guild guild = ctx.getGuild();
			MessageChannel channel = ctx.getChannel();
			List<Warnings> warnings = new Warning(guild, user, ctx.getIGSQBot()).get();
			StringBuilder stringBuilder = new StringBuilder();

			warnings.forEach(warn -> stringBuilder
					.append("**ID: ")
					.append(warn.getId())
					.append("** ")
					.append(warn.getWarnText())
					.append(" - ")
					.append(StringUtils.parseDateTime(warn.getTimestamp()))
					.append("\n"));

			channel.sendMessage(new EmbedBuilder()
					.setTitle("Warnings for " + user.getAsTag())
					.setDescription(stringBuilder.length() == 0 ? "This user has no warnings" : stringBuilder.toString())
					.setColor(Constants.IGSQ_PURPLE)
					.build()).queue();
		});
	}
}
