package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.List;


public class SuggestionCommand extends Command
{
    public SuggestionCommand()
    {
        super("Suggestion", new String[]{"suggest", "suggestion", "idea"}, "Suggests an idea in the designated suggestion channel","[topic]", new Permission[]{}, true, 0);
    }

    @Override
    public void execute(List<String> args, CommandContext ctx)
    {
        final Guild guild = ctx.getGuild();
        final User author = ctx.getAuthor();
        final MessageChannel channel = ctx.getChannel();

        if(args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(channel, this);
            return;
        }
        final TextChannel suggestionChannel = guild.getTextChannelById(Yaml.getFieldString(guild.getId() + ".suggestionchannel", "guild"));
        if(suggestionChannel != null)
        {
            channel.sendMessage(suggestionChannel.getName());
            suggestionChannel.sendMessage("hello world");
            if(!UserUtils.basicPermCheck(guild.getSelfMember(), suggestionChannel))
            {
                EmbedUtils.sendPermissionError(channel, this);
            }
            else if(CommandUtils.isCommandTooLarge(args))
            {
                EmbedUtils.sendSyntaxError(channel, this);
            }
            else
            {
                new EmbedGenerator((MessageChannel) suggestionChannel)
                        .title("Suggestion:")
                        .text(ArrayUtils.arrayCompile(args, " "))
                        .color(EmbedUtils.IGSQ_PURPLE)
                        .thumbnail(author.getAvatarUrl())
                        .footer("Suggestion by: " + UserUtils.getMemberFromUser(author, guild).getEffectiveName() + author.getDiscriminator())
                        .send();
            }
        }
        else
        {
            EmbedUtils.sendError(channel, "There is no setup suggestion Channel");
        }
    }
}