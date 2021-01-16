package org.igsq.igsqbot.commands.moderation;

import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.Warning;
import org.igsq.igsqbot.entities.jooq.tables.pojos.Warnings;
import org.igsq.igsqbot.util.*;

public class WarnCommand extends Command
{
    @Override
    public void execute(List<String> args, CommandContext ctx)
    {
        MessageChannel channel = ctx.getChannel();
        Guild guild = ctx.getGuild();
        User author = ctx.getAuthor();
        if (args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(ctx);
            return;
        }

        String firstArg = args.get(0);
        int argsSize = args.size();
        args.remove(0);
        if (firstArg.equalsIgnoreCase("show"))
        {
            if (argsSize != 2)
            {
                EmbedUtils.sendSyntaxError(ctx);
                return;
            }
            new Parser(args.get(0), ctx).parseAsUser(user ->
            {
                List<Warnings> warnings = showWarnings(user, guild, ctx);
                StringBuilder stringBuilder = new StringBuilder();

                warnings.forEach(warn -> stringBuilder
                        .append("**ID: ")
                        .append(warn.getWarnid())
                        .append("** ")
                        .append(warn.getWarntext())
                        .append(" - ")
                        .append(StringUtils.parseDateTime(warn.getTimestamp()))
                        .append("\n"));

                channel.sendMessage(new EmbedBuilder()
                        .setTitle("Warnings for " + user.getAsTag())
                        .setDescription(stringBuilder.length() == 0 ? "This user has no warnings" : stringBuilder.toString())
                        .setColor(Constants.IGSQ_PURPLE)
                        .build()).queue();
            });

        }
        else if (firstArg.equalsIgnoreCase("remove"))
        {
            if (args.size() != 2)
            {
                EmbedUtils.sendSyntaxError(ctx);
                return;
            }

            new Parser(args.get(0), ctx).parseAsUser(user ->
                    {
                        if (user.isBot())
                        {
                            EmbedUtils.sendError(channel, "Bots cannot be warned.");
                        }
                        else
                        {
                            CommandUtils.interactionCheck(author, user, ctx, () ->
                            {
                                OptionalInt warningNumber = new Parser(args.get(1), ctx).parseAsUnsignedInt();
                                if (warningNumber.isEmpty())
                                {
                                    ctx.replyError("Invalid warning specified.");
                                }
                                else
                                {
                                    Warnings warn = new Warning(ctx.getGuild(), user, ctx.getIGSQBot()).getById(warningNumber.getAsInt());

                                    if (warn == null)
                                    {
                                        ctx.replyError("Invalid warning specified.");
                                    }
                                    else
                                    {
                                        removeWarning(user, guild, ctx, warn.getWarnid());
                                        ctx.replySuccess("Removed warning: " + warn.getWarntext());
                                    }
                                }
                            });
                        }
                    }
            );
        }
        else
        {
            new Parser(firstArg, ctx).parseAsUser(user ->
            {
                if (user.isBot())
                {
                    EmbedUtils.sendError(channel, "Bots cannot be warned.");
                }
                else
                {
                    CommandUtils.interactionCheck(author, user, ctx, () -> addWarning(user, guild, ctx, ArrayUtils.arrayCompile(args, " ")));
                }
            });
        }
    }

    private void addWarning(User user, Guild guild, CommandContext ctx, String reason)
    {
        new Warning(guild, user, ctx.getIGSQBot()).add(reason);
        ctx.replySuccess("Warned " + user.getAsMention() + " for reason: " + reason);
    }

    private List<Warnings> showWarnings(User user, Guild guild, CommandContext ctx)
    {
        return new Warning(guild, user, ctx.getIGSQBot()).get();
    }

    private void removeWarning(User user, Guild guild, CommandContext ctx, long number)
    {
        new Warning(guild, user, ctx.getIGSQBot()).remove(number);
    }

    @Override
    public String getName()
    {
        return "Warn";
    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList("warn");
    }

    @Override
    public String getDescription()
    {
        return "Handles the user warning system";
    }

    @Override
    public String getSyntax()
    {
        return "[user][reason] | [show][user] | [remove][user][number]";
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
    public long getCooldown()
    {
        return 0;
    }
}
