package org.igsq.igsqbot.commands.subcommands.warning;

import java.util.List;
import java.util.OptionalInt;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

public class WarningRemoveCommand extends Command
{
	public WarningRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes a warning", "[user][id]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
			return;
		}

		MessageChannel channel = ctx.getChannel();
		User author = ctx.getAuthor();
		Guild guild = ctx.getGuild();
		new Parser(args.get(0), ctx).parseAsUser(user ->
				{
					if(user.isBot())
					{
						EmbedUtils.sendError(channel, "Bots cannot be warned.");
					}
					else
					{
						CommandUtils.interactionCheck(author, user, ctx, () ->
						{
							OptionalInt warningNumber = new Parser(args.get(1), ctx).parseAsUnsignedInt();
							if(warningNumber.isPresent())
							{
								Warnings warn = new Warning(ctx.getGuild(), user, ctx.getIGSQBot()).getById(warningNumber.getAsInt());

								if(warn == null)
								{
									ctx.replyError("Invalid warning specified.");
								}
								else
								{
									new Warning(guild, user, ctx.getIGSQBot()).remove(warningNumber.getAsInt());
									ctx.replySuccess("Removed warning: " + warn.getWarntext());
								}
							}
						});
					}
				}
		);
	}
}
