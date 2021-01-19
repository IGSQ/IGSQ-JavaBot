package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.commands.subcommands.warning.WarningRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.warning.WarningShowCommand;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.Parser;

@SuppressWarnings("unused")
public class WarnCommand extends Command
{
	public WarnCommand()
	{
		super("Warn", "Handles the user warning system", "[user][reason] | [show][user] | [remove][user][number]");
		addAliases("warn");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addChildren(
				new WarningShowCommand(this),
				new WarningRemoveCommand(this));
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsSizeSubceeds(ctx, 2);

		User author = ctx.getAuthor();
		Guild guild = ctx.getGuild();

		new Parser(args.get(0), ctx).parseAsUser(user ->
		{
			if(user.isBot())
			{
				throw new CommandResultException("Bots cannot be warned.");
			}
			CommandUtils.interactionCheck(author, user, ctx, () ->
			{
				args.remove(0);
				String reason = String.join(" ", args);
				new Warning(guild, user, ctx.getIGSQBot()).add(reason);
				ctx.replySuccess("Warned " + user.getAsMention() + " for reason: " + reason);
			});
		});

	}
}