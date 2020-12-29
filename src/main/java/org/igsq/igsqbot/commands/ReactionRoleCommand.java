package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.yaml.ReactionRole;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReactionRoleCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Message message = ctx.getMessage();
		final Guild guild = ctx.getGuild();

		if(args.size() != 5 || message.getMentionedRoles().isEmpty() || message.getMentionedChannels().isEmpty())
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

			if(emoji != null && !emoji.matches("/[^\\w$\\x{0080}-\\x{FFFF}]+/u"))
			{
				EmbedUtils.sendSyntaxError(channel, this);
				return;
			}

			if(!ctx.getMember().canInteract(role))
			{
				EmbedUtils.sendPermissionError(channel, this);
				return;
			}
			reactionChannel.retrieveMessageById(messageId).queue(
					reactionMessage ->
					{
						ReactionRole rr = new ReactionRole(guild.getId(), reactionChannel.getId(), reactionMessage.getId());
						switch(action.toLowerCase())
						{
							case "add":
								if(emote != null && !rr.isEmoteMapped(emote))
								{
									if(emote.canInteract(guild.getSelfMember().getUser(), reactionChannel))
									{
										reactionMessage.addReaction(emote).queue(success -> rr.setReaction(emote, role.getId()), error -> EmbedUtils.sendError(channel, "I cannot add the reaction: " + emote.getAsMention()));
									}
									else
									{
										EmbedUtils.sendError(channel, "I cannot use that emote.");
									}
								}
								else if(emoji != null && !rr.isEmojiMapped(emoji))
								{
									reactionMessage.addReaction(emoji).queue(success -> rr.setReaction(emoji, role.getId()), error -> EmbedUtils.sendError(channel, "I cannot add the reaction: " + emoji));
								}
								else
								{
									EmbedUtils.sendError(channel, "That emote / emoji is already mapped to a role.");
								}
								break;

							case "remove":
								if(emote != null)
								{
									if(emote.canInteract(guild.getSelfMember().getUser(), reactionChannel))
									{
										reactionMessage.clearReactions(emote).queue(success -> rr.removeReaction(emote, role.getId()), error -> EmbedUtils.sendError(channel, "An error occurred while removing the reaction role."));
									}
									else
									{
										EmbedUtils.sendError(channel, "I cannot use that emote.");
									}
								}
								else if(emoji != null)
								{
									reactionMessage.clearReactions(emoji).queue(success -> rr.removeReaction(emoji, role.getId()), error -> EmbedUtils.sendError(channel, "An error occurred while removing the reaction role."));
								}
								else
								{
									EmbedUtils.sendError(channel, "That emote / emoji is already mapped to a role.");
								}
								break;
							case "clear":
								rr.clear();
								reactionMessage.clearReactions().queue(success -> EmbedUtils.sendSuccess(channel, "Removed all reactionroles."), error -> EmbedUtils.sendError(channel, "An error occurred while removing the reaction role."));

								break;
							default:
								EmbedUtils.sendSyntaxError(channel, this);
						}


					},
					error -> EmbedUtils.sendError(channel, "The specified message ID was invalid.")
			);
		}
	}

	@Override
	public String getName()
	{
		return "ReactionRole";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("reactionrole", "reactionroles", "rr");
	}

	@Override
	public String getDescription()
	{
		return "Controls reactionroles.";
	}

	@Override
	public String getSyntax()
	{
		return "[add|remove|clear] [messageID] [channel] [role] [reaction]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.hasPermission(Collections.singletonList(Permission.ADMINISTRATOR));
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

