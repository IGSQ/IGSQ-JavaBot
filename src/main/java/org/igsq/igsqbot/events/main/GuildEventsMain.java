package org.igsq.igsqbot.events.main;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.util.DatabaseUtils;

public class GuildEventsMain extends ListenerAdapter
{
    private final IGSQBot igsqBot;

    public GuildEventsMain(IGSQBot igsqBot)
    {
        this.igsqBot = igsqBot;
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event)
    {
        DatabaseUtils.removeGuild(event.getGuild(), igsqBot);
    }

    @Override
    public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
    {
        DatabaseUtils.removeGuild(event.getGuildIdLong(), igsqBot);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event)
    {
        //TO BE IMPLEMENTED
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        DatabaseUtils.registerGuild(event.getGuild(), igsqBot);
    }
}

