package org.igsq.igsqbot.entities.cache;

import net.dv8tion.jda.api.entities.User;

public class CachedUser
{
    private final boolean isBot;
    private final String mentionable;

    public CachedUser(User user)
    {
        this.isBot = user.isBot();
        this.mentionable = user.getAsMention();
    }

    public boolean isBot()
    {
        return isBot;
    }

    public String getAsMention()
    {
        return mentionable;
    }
}
