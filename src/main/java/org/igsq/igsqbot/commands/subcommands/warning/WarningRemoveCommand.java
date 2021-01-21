package org.igsq.igsqbot.commands.subcommands.warning;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.Parser;

public class WarningRemoveCommand extends Command
{
	public WarningRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a warning.", "[user][warningID]");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addFlags(CommandFlag.GUILD_ONLY);
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
						failure.accept(new CommandInputException("Bots cannot have warnings."));
					}

					CommandUtils.interactionCheck(author, user, cmd, () ->
					{
						OptionalInt warningNumber = new Parser(args.get(1), cmd).parseAsUnsignedInt();
						if(warningNumber.isPresent())
						{
							Warnings warn = new Warning(cmd.getGuild(), user, cmd.getIGSQBot()).getByWarnId(warningNumber.getAsInt());

							if(warn == null)
							{
								failure.accept(new CommandInputException("Invalid warning specified."));
								return;
							}

							new Warning(guild, user, cmd.getIGSQBot()).remove(warningNumber.getAsInt());
							cmd.replySuccess("Removed warning: " + warn.getWarnText());
						}
					});
				}
		);
	}
}
