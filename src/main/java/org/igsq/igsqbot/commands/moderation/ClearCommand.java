package org.igsq.igsqbot.commands.moderation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.util.EmbedUtils;

public class ClearCommand extends Command
{
    @Override
    public void execute(List<String> args, CommandContext ctx)
    {
        int amount;
        MessageChannel channel = ctx.getChannel();
        User author = ctx.getAuthor();
        Guild guild = ctx.getGuild();

        try
        {
            amount = Integer.parseInt(args.get(0));
        }
        catch (Exception exception)
        {
            EmbedUtils.sendSyntaxError(ctx);
            return;
        }
        if (amount <= 0)
        {
            EmbedUtils.sendSyntaxError(ctx);
        }
        else if (amount > 51)
        {
            EmbedUtils.sendSyntaxError(ctx);
        }
        else if (CooldownHandler.isOnCooldown(author.getId(), this))
        {
            return;
        }
        else
        {
            channel.getIterableHistory().takeAsync(amount).thenAccept(messages ->
            {
                CooldownHandler.addCooldown(author.getId(), this);
                channel.purgeMessages(messages);
                ctx.replySuccess("Deleted " + (messages.size()) + " messages");
                MessageCache cache = MessageCache.getCache(guild);
                messages.stream().filter(cache::isInCache).forEach(cache::remove);
            });
        }
    }

    @Override
    public String getName()
    {
        return "Clear";
    }

    @Override
    public List<String> getAliases()
    {
        return Arrays.asList("clear", "purge");
    }

    @Override
    public String getDescription()
    {
        return "Clears the channel with the specified amount";
    }

    @Override
    public String getSyntax()
    {
        return "[amount {50}]";
    }

    @Override
    public boolean canExecute(CommandContext ctx)
    {
        return ctx.hasPermission(Collections.singletonList(Permission.MESSAGE_MANAGE));
    }

    @Override
    public boolean isGuildOnly()
    {
        return true;
    }

    @Override
    public long getCooldown()
    {
        return 10000;
    }
}
