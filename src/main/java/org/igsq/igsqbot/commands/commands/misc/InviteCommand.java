package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandResultException;

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
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
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
					failure.accept(new CommandResultException("No invites found."));
				}
		);
	}
}