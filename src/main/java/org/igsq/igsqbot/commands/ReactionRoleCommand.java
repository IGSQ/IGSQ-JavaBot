package org.igsq.igsqbot.commands;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.json.JsonGuild;
import org.igsq.igsqbot.entities.json.JsonGuildCache;
import org.igsq.igsqbot.entities.json.JsonReactionRole;
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
			String action = args.get(0);
			String messageId = args.get(1);
			Role role = ctx.getMessage().getMentionedRoles().get(0);
			MessageChannel reactionChannel = message.getMentionedChannels().get(0);
			JsonGuild jsonGuild = JsonGuildCache.getInstance().get(guild.getId());

			if(jsonGuild != null)
			{
				String emoteId;

				if(!message.getEmotes().isEmpty())
				{
					emoteId = message.getEmotes().get(0).getId();
				}
				else
				{
					List<String> emojis = EmojiParser.extractEmojis(args.get(4));
					if(emojis.isEmpty())
					{
						EmbedUtils.sendSyntaxError(channel, this);
						return;
					}
					else
					{
						emoteId = emojis.get(0);
					}
				}

				if(!ctx.getMember().canInteract(role))
				{
					EmbedUtils.sendPermissionError(channel, this);
					return;
				}

				reactionChannel.retrieveMessageById(messageId).queue(
						reactionMessage ->
						{
							if(action.equalsIgnoreCase("add"))
							{
								reactionMessage.addReaction(emoteId).queue(
										success ->
										{
											JsonReactionRole reactionRole = new JsonReactionRole(guild.getId(), reactionChannel.getId(), reactionMessage.getId(), emoteId, role.getId());
											List<JsonReactionRole> reactionRoles = jsonGuild.getReactionRoles();
											reactionRoles.add(reactionRole);
											jsonGuild.setReactionRoles(reactionRoles);
										},
										failure -> EmbedUtils.sendError(channel, "That emote could not be found.")
								);
							}
							else if(action.equalsIgnoreCase("remove"))
							{
								reactionMessage.removeReaction(emoteId).queue(
										success ->
										{
											List<JsonReactionRole> reactionRoles = jsonGuild.getReactionRoles();
											reactionRoles.removeIf(rr -> rr.getPrimaryKey().equals(emoteId));
											jsonGuild.setReactionRoles(reactionRoles);
										},
										failure -> EmbedUtils.sendError(channel, "That emote could not be removed.")
								);
							}
						},
						error -> EmbedUtils.sendError(channel, "The specified message ID was invalid.")
				);
			}
			else
			{
				EmbedUtils.sendError(channel, "An error occurred while adding the reaction role.");
			}
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

