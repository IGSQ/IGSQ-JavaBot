package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Arrays;
import java.util.List;

public class InviteCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
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

	@Override
	public String getName()
	{
		return "Invite";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("invite", "inv");
	}

	@Override
	public String getDescription()
	{
		return "Shows the best invite for the guild.";
	}

	@Override
	public String getSyntax()
	{
		return "[none]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
