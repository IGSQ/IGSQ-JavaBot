package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.util.*;

public class ReactionRoleRemoveCommand extends Command
{
    public ReactionRoleRemoveCommand(Command parent)
    {
        super(parent, "remove", "Removes reaction roles", "[messageId][channel][role][emote]");
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
            OptionalLong messageId = new Parser(args.get(0), ctx).parseAsUnsignedLong();
            String emote = ctx.getMessage().getEmotes().isEmpty() ? args.get(3) : ctx.getMessage().getEmotes().get(0).getId();
            new Parser(args.get(2), ctx).parseAsRole(role ->
            {
                if(messageId.isPresent())
                {
                    new Parser(args.get(1), ctx).parseAsTextChannel(
                    channel ->
                    {
                        channel.retrieveMessageById(messageId.getAsLong()).queue(
                        message ->
                        {
                            ReactionRole reactionRole = new ReactionRole(messageId.getAsLong(), role.getIdLong(), ctx.getGuild().getIdLong(), emote, ctx.getIGSQBot());

                            if(!reactionRole.isPresent())
                            {
                                ctx.replyError("That reaction role does not exist");
                            }
                            else
                            {
                                reactionRole.remove();
                                ctx.replySuccess("Removed reaction role for role " + StringUtils.getRoleAsMention(role.getIdLong()));
                                message.clearReactions(emote).queue();
                            }
                        },
                        error -> ctx.replyError("That message does not exist"));
                    });
                }
            });
        }
    }
}
