package org.igsq.igsqbot.commands.subcommands.warning;

import java.util.List;
import java.util.OptionalInt;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.exception.CommandResultException;
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
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsSizeSubceeds(ctx, 2);

		User author = ctx.getAuthor();
		Guild guild = ctx.getGuild();
		new Parser(args.get(0), ctx).parseAsUser(user ->
				{
					if(user.isBot())
					{
						throw new CommandResultException("Bots cannot have warnings.");
					}

					CommandUtils.interactionCheck(author, user, ctx, () ->
					{
						OptionalInt warningNumber = new Parser(args.get(1), ctx).parseAsUnsignedInt();
						if(warningNumber.isPresent())
						{
							Warnings warn = new Warning(ctx.getGuild(), user, ctx.getIGSQBot()).getByWarnId(warningNumber.getAsInt());

							if(warn == null)
							{
								throw new CommandResultException("Invalid warning specified.");
							}

							new Warning(guild, user, ctx.getIGSQBot()).remove(warningNumber.getAsInt());
							ctx.replySuccess("Removed warning: " + warn.getWarnText());
						}
					});
				}
		);
	}
}
