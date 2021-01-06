package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class InviteCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();
		Guild guild = ctx.getGuild();

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
			guild.retrieveInvites().queue(
					invites ->
					{
						AtomicReference<Invite> chosenInvite = new AtomicReference<>();
						invites.forEach(invite -> invite.expand().queue(
								expandedInvite ->
								{
									if(expandedInvite.getMaxUses() == 0 && chosenInvite.get() == null)
									{
										chosenInvite.set(expandedInvite);
									}
								}
						));

						if(chosenInvite.get() == null)
						{
							ctx.replyError("No invites found.");
						}
						else
						{
							ctx.replySuccess("Invite found: " + chosenInvite.get().getUrl());
						}
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
