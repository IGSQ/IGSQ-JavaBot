package org.igsq.igsqbot.commands.commands.moderation;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.commands.subcommands.warning.WarningRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.warning.WarningShowCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.Parser;

@SuppressWarnings("unused")
public class WarnCommand extends Command
{
	public WarnCommand()
	{
		super("Warn", "Handles the user warning system", "[user][reason] / [show][user] / [remove][user][number]");
		addAliases("warn");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addChildren(
				new WarningShowCommand(this),
				new WarningRemoveCommand(this));
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(cmd, 2, failure)) return;

		User author = cmd.getAuthor();
		Guild guild = cmd.getGuild();

		new Parser(args.get(0), cmd).parseAsUser(user ->
		{
			if(user.isBot())
			{
				failure.accept(new CommandResultException("Bots cannot be warned."));
			}
			CommandUtils.interactionCheck(author, user, cmd, () ->
			{
				args.remove(0);
				String reason = String.join(" ", args);
				new Warning(guild, user, cmd.getIGSQBot()).add(reason);
				cmd.replySuccess("Warned " + user.getAsMention() + " for reason: " + reason);

				user.openPrivateChannel()
						.flatMap(privateChannel -> privateChannel.sendMessage(new EmbedBuilder()
								.setTitle("You have been warned in " + guild.getName())
								.addField("Reason", reason, true)
								.addField("Moderator", author.getAsMention(), true)
								.setColor(Constants.IGSQ_PURPLE)
								.setTimestamp(Instant.now())
								.build())).queue(null, error -> {});
			});
		});
	}
}