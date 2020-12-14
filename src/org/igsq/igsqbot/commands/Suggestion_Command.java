package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;


public class Suggestion_Command extends Command
{
    public Suggestion_Command()
    {
        super("suggest", new String[]{}, "Suggests an idea in the designated suggestion channel", new Permission[]{}, true, 0);
    }

    @Override
    public void execute(String[] args, Context ctx)
    {
        final Guild guild = ctx.getGuild();
        final User author = ctx.getAuthor();
        final MessageChannel channel = ctx.getChannel();

        final MessageChannel suggestionChannel = guild.getTextChannelById(Yaml.getFieldString(guild.getId() + ".suggestionchannel", "guild"));

        if(suggestionChannel != null)
        {
            if(!guild.getSelfMember().hasPermission((GuildChannel) channel, Permission.MESSAGE_WRITE) || !guild.getSelfMember().hasAccess((GuildChannel) suggestionChannel))
            {
                EmbedUtils.sendError(channel, "I cannot access / write into to the suggestion channel: " + StringUtils.getChannelAsMention(suggestionChannel.getId()));
            }
            else
            {
                new EmbedGenerator(suggestionChannel)
                        .title("Suggestion:")
                        .text(args[0])
                        .color(EmbedUtils.IGSQ_PURPLE)
                        .thumbnail(author.getAvatarUrl())
                        .footer("Suggestion by: " + UserUtils.getMemberFromUser(author, guild).getNickname())
                        .send();
            }
        }
        else
        {
            EmbedUtils.sendError(channel, "There is no setup suggestion Channel");
        }
    }
}