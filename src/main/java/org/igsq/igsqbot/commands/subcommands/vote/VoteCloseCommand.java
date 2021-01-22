package org.igsq.igsqbot.commands.subcommands.vote;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Vote;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;

public class VoteCloseCommand extends Command
{
	public VoteCloseCommand(Command parent)
	{
		super(parent, "close", "Closes a vote.", "[voteID]");
		addMemberPermissions(Permission.MANAGE_CHANNEL);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(cmd, failure)) return;

		OptionalLong id = new Parser(args.get(0), cmd).parseAsUnsignedLong();

		if(id.isPresent())
		{
			Boolean success = Vote.closeById(id.getAsLong(), cmd);
			if(success == null)
			{
				return;
			}

			if(!success)
			{
				failure.accept(new CommandResultException("Vote with ID **" + id.getAsLong() + "** not found."));
			}
			else
			{
				cmd.replySuccess("Closed vote **" + id.getAsLong() + "** successfully.");
			}
		}
	}
}
