package org.igsq.igsqbot.commands.subcommands.verification;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.VerificationUtils;

public class VerificationRemoveCommand extends Command
{
	public VerificationRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a phrase/role pair.", "[phrase{2}][role]");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeExceeds(cmd, 3, failure)) return;

		new Parser(args.get(0), cmd).parseAsRole(
				role ->
				{
					String phrase = ArrayUtils.arrayCompile(args.subList(1, args.size()), " ");

					if(VerificationUtils.removeMapping(phrase, cmd.getGuildIdLong(), role.getIdLong(), cmd.getIGSQBot()))
					{
						cmd.replySuccess("Remove mapping " + phrase + " for role " + role.getAsMention());
					}
					else
					{
						cmd.replyError("Mapping " + phrase + " for role " + role.getAsMention() + " does not exist.");
					}
				});
	}
}
