package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;

import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

public class ReactionRoleAddCommand extends Command
{
    public ReactionRoleAddCommand(Command parent)
    {
        super(parent, "add", "Adds a reaction role", "[messageId][channel][role][emote]");
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
		if(args.size() < 4)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			String emote;
			if(!ctx.getMessage().getEmotes().isEmpty())
			{
				if(ctx.getMessage().getEmotes().get(0).isAnimated())
				{
					ctx.replyError("Animated emotes are not allowed.");
					return;
				}
				emote = ctx.getMessage().getEmotes().get(0).getId();
			}
			else
			{
				emote = args.get(3);
			}

			new Parser(args.get(1), ctx).parseAsTextChannel(
			channel ->
			{
				channel.retrieveMessageById(args.get(0)).queue(
				message ->
				{
					new Parser(args.get(2), ctx).parseAsRole(
					role ->
					{
						message.addReaction(emote).queue(
						success ->
						{
							new ReactionRole(message.getIdLong(), role.getIdLong(), ctx.getGuild().getIdLong(), emote, ctx.getIGSQBot()).add();
							ctx.replySuccess("Reaction role added.");
						},
						error -> ctx.replyError("An error occurred while adding the reaction."));
					});
				});
			});
		}
    }
}
