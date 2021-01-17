package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.CommandFlag;
import org.igsq.igsqbot.util.EmbedUtils;

public class InviteCommand extends Command
{
	public InviteCommand()
	{
		super("Invite", "Shows the best invite for the server.", "[none]");
		addFlags(CommandFlag.GUILD_ONLY);
		addAliases("invite");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		Guild guild = ctx.getGuild();

		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else if(!guild.getSelfMember().hasPermission(Permission.MANAGE_SERVER))
		{
			EmbedUtils.sendPermissionError(ctx);
		}
		else
		{
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
}