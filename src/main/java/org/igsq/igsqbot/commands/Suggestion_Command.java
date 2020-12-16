package main.java.org.igsq.igsqbot.commands;

import main.java.org.igsq.igsqbot.Yaml;
import main.java.org.igsq.igsqbot.objects.Command;
import main.java.org.igsq.igsqbot.objects.Context;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.util.ArrayUtils;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import main.java.org.igsq.igsqbot.util.UserUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;


public class Suggestion_Command extends Command
{
    public Suggestion_Command()
    {
        super("suggestion", new String[]{"suggest", "idea"}, "Suggests an idea in the designated suggestion channel","[topic]", new Permission[]{}, true, 0);
    }

    @Override
    public void execute(List<String> args, Context ctx)
    {
        final Guild guild = ctx.getGuild();
        final User author = ctx.getAuthor();
        final MessageChannel channel = ctx.getChannel();

        if(args.isEmpty())
        {
            EmbedUtils.sendSyntaxError(channel, this);
            return;
        }
        final GuildChannel suggestionChannel = guild.getTextChannelById(Yaml.getFieldString(guild.getId() + ".suggestionchannel", "guild"));
        if(suggestionChannel != null)
        {
            if(UserUtils.basicPermCheck(guild.getSelfMember(), suggestionChannel))
            {
                EmbedUtils.sendPermissionError(channel, this);
            }
            else
            {
                new EmbedGenerator((MessageChannel) suggestionChannel)
                        .title("Suggestion:")
                        .text(ArrayUtils.arrayCompile(args, " "))
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