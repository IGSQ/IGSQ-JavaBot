package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;

public class ReactionRoleCommand extends Command
{
	public ReactionRoleCommand()
	{
		super("ReactionRoles", new String[]{"rr", "reactionroles", "reactionrole"}, "Controls reactionroles.", "[add|remove][messageID][channel][role][reaction]", new Permission[]{}, true, 0);
	}
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Message message = ctx.getMessage();
		if(args.size() != 4 || message.getMentionedRoles().isEmpty() || message.getMentionedChannels().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
			final String action = args.get(0);
			final String messageId = args.get(1);
			final Role role = ctx.getMessage().getMentionedRoles().get(0);
			final MessageChannel reactionChannel = message.getMentionedChannels().get(0);

			final Emote emote;
			final String emoji;
			if(!message.getEmotes().isEmpty())
			{
				emote = message.getEmotes().get(0);
				emoji = null;
			}
			else
			{
				emoji = args.get(4);
				emote = null;
			}

			reactionChannel.retrieveMessageById(messageId).queue(
					reactionMessage ->
					{
						switch(action.toLowerCase())
						{
							case "add":
								if(emoji != null)
								{
									addReactionRole(message, role, emoji);
								}
								else
								{
									addReactionRole(message, role, emote);
								}
							case "remove":
								if(emoji != null)
								{
									removeReactionRole(message, role, emoji);
								}
								else
								{
									removeReactionRole(message, role, emote);
								}
							default:
								EmbedUtils.sendSyntaxError(channel, this);
						}
					},
					error -> EmbedUtils.sendError(channel, "The specified message ID was invalid.")
			);

		}
	}

	private void addReactionRole(Message message, Role role, Emote reaction)
	{

	}

	private void removeReactionRole(Message message, Role role, Emote reaction)
	{

	}

	private void addReactionRole(Message message, Role role, String reaction)
	{

	}

	private void removeReactionRole(Message message, Role role, String reaction)
	{

	}
}

