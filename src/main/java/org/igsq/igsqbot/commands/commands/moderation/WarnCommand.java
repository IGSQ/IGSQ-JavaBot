package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.commands.subcommands.warning.WarningRemoveCommand;
import org.igsq.igsqbot.commands.subcommands.warning.WarningShowCommand;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.CommandFlag;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

@SuppressWarnings("unused")
public class WarnCommand extends Command
{
	public WarnCommand()
	{
		super("Warn", "Handles the user warning system", "[user][reason] | [show][user] | [remove][user][number]");
		addAliases("warn");
		addPermissions(Permission.MESSAGE_MANAGE);
		addChildren(
				new WarningShowCommand(this),
				new WarningRemoveCommand(this));
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		if(args.size() < 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			User author = ctx.getAuthor();
			Guild guild = ctx.getGuild();
			MessageChannel channel = ctx.getChannel();
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
						args.remove(0);
						String reason = String.join(" ", args);
						new Warning(guild, user, ctx.getIGSQBot()).add(reason);
						ctx.replySuccess("Warned " + user.getAsMention() + " for reason: " + reason);
					});
				}
			});
		}
	}
}