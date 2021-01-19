package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.util.CommandChecks;

@SuppressWarnings("unused")
public class InviteCommand extends Command
{
	public InviteCommand()
	{
		super("Invite", "Shows the best invite for the server.", "[none]");
		addFlags(CommandFlag.GUILD_ONLY);
		addSelfPermissions(Permission.MANAGE_SERVER);
		addAliases("invite");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsEmpty(ctx);
		Guild guild = ctx.getGuild();

		guild.retrieveInvites().queue(
				invites ->
				{
					for(Invite invite : invites)
					{
						if(invite.getMaxUses() == 0)
						{
							ctx.replySuccess("Invite found: " + invite.getUrl());
							return;
						}
					}
					ctx.replyError("No invites found.");
				}
		);

	}
}