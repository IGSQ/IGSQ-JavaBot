package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;

public class ReactionRoleShowCommand extends Command
{
	public ReactionRoleShowCommand(Command parent)
	{
		super(parent, "show", "Shows reaction roles", "[messageId]");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			OptionalLong messageId = new Parser(args.get(0), ctx).parseAsUnsignedLong();

			if(messageId.isPresent())
			{
				List<ReactionRole> reactionRoles = ReactionRole.getByMessageId(messageId.getAsLong(), ctx.getIGSQBot());
				StringBuilder text = new StringBuilder();

				for(ReactionRole reactionRole : reactionRoles)
				{
					text
							.append(StringUtils.getEmoteAsMention(reactionRole.getEmote()))
							.append("  ")
							.append(StringUtils.getRoleAsMention(reactionRole.getRoleId()))
							.append("  ")
							.append(reactionRole.getMessageId());
				}

				ctx.getChannel().sendMessage(new EmbedBuilder()
						.setTitle("Reaction roles for message " + messageId.getAsLong())
						.setDescription(text.length() == 0 ? "No reaction roles found" : text.toString())
						.setColor(Constants.IGSQ_PURPLE)
						.build()).queue();
			}
		}
	}
}
