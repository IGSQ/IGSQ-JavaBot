package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.InviteAction;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
			guild.retrieveInvites().queue(
					invites ->
					{
						AtomicReference<Invite> chosenInvite = new AtomicReference<>();
						invites.forEach(invite -> invite.expand().queue(
								expandedInvite ->
								{
									if(!expandedInvite.isTemporary() && expandedInvite.getMaxUses() == 0 && chosenInvite.get() == null)
									{
										chosenInvite.set(expandedInvite);
									}
								}
						));

						if(chosenInvite.get() == null)
						{
							createInvite(guild).queue(newInvite -> EmbedUtils.sendSuccess(channel, "Created new invite: " + newInvite.getUrl()));
						}
						else
						{
							EmbedUtils.sendSuccess(channel, "Invite found: " + chosenInvite.get().getUrl());
						}
					}

			);
		}
	}

	private InviteAction createInvite(Guild guild)
	{
		List<GuildChannel> channels = guild.getChannels().stream().filter(channel -> !channel.getType().equals(ChannelType.CATEGORY)).collect(Collectors.toList());
		return channels.get(0).createInvite().setMaxUses(0).setMaxAge(0);
	}
}
