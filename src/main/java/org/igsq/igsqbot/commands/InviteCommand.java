package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class InviteCommand extends Command
{
	public InviteCommand()
	{
		super("Invite", new String[]{"invite", "inv"}, "Shows the best invite for the guild, or makes a new one.", "[none]", new Permission[]{}, true, 0);
	}
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Guild guild = ctx.getGuild();

		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(!guild.getSelfMember().hasPermission(Permission.MANAGE_SERVER))
		{
			EmbedUtils.sendPermissionError(channel, this);
		}
		else
		{
			AtomicBoolean isFound = new AtomicBoolean(false);
			guild.retrieveInvites().queue(
					invites ->
					{
						invites.forEach(invite -> invite.expand().queue(
								expandedInvite ->
								{
									if(!expandedInvite.isTemporary() && expandedInvite.getMaxUses() == 0 && !isFound.get())
									{
										EmbedUtils.sendSuccess(channel, "Invite found: " + expandedInvite.getUrl());
										isFound.set(true);
									}
								}
						));

						if(!isFound.get())
						{
							EmbedUtils.sendError(channel, "No invites found.");
						}
					}

			);
		}
	}
}
