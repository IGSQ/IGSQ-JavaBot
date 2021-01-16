package org.igsq.igsqbot.commands2.commands.misc;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.commands2.subcommands.info.BotInfoCommand;
import org.igsq.igsqbot.commands2.subcommands.info.RoleInfoCommand;
import org.igsq.igsqbot.commands2.subcommands.info.ServerInfoCommand;
import org.igsq.igsqbot.commands2.subcommands.info.UserInfoCommand;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.entities.info.MemberInfo;
import org.igsq.igsqbot.util.StringUtils;

public class InfoCommand extends NewCommand
{
    public InfoCommand()
    {
        super("Info", "Provides information about things.", "[user|server|bot|role]");
        addAliases("info");
        addChildren(
                new UserInfoCommand(this),
                new BotInfoCommand(this),
                new RoleInfoCommand(this),
                new ServerInfoCommand(this));
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        if (args.isEmpty())
        {
            MemberInfo memberInfo = new MemberInfo(ctx.getMember());
            ctx.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Info for " + memberInfo.getNickname())
                    .addField("Joined at", StringUtils.parseDateTime(memberInfo.getTimeJoined()), false)
                    .addField("Boosting", memberInfo.isBoosting()
                            ? "Since" + StringUtils.parseDateTime(memberInfo.getBoostingSince())
                            : "No", false)
                    .setThumbnail(memberInfo.getAvatarURL())
                    .build()).queue();
        }
    }
}

