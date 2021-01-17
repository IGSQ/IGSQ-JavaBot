package org.igsq.igsqbot.commands.subcommands.info;

import java.util.List;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.CommandFlag;
import org.igsq.igsqbot.entities.info.GuildInfo;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;

public class ServerInfoCommand extends Command
{
	public ServerInfoCommand(Command parent)
	{
		super(parent, "server", "Shows information about a server", "[server]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		Optional<Guild> guild;
		if(args.isEmpty())
		{
			guild = Optional.of(ctx.getGuild());
		}
		else
		{
			guild = new Parser(args.get(0), ctx).parseAsGuild();
		}

		if(guild.isPresent())
		{
			GuildInfo guildInfo = new GuildInfo(guild.get());

			ctx.getChannel().sendMessage(new EmbedBuilder()
					.setTitle("Information for server: **" + guildInfo.getName() + "**")
					.addField("Partner status", guildInfo.isPartner() ? "Is partnered" : "Not partnered", true)
					.addField("Verified Status", guildInfo.isVerified() ? "Is verified" : "Not verified", true)
					.addField("Public status", guildInfo.isPublic() ? "Is public" : "Not public", true)
					.addField("Boost count", String.valueOf(guildInfo.getBoosts()), true)
					.addField("Member count", guildInfo.getMemberCount() + " / " + guildInfo.getMaxMemberCount(), true)
					.addField("Created at", StringUtils.parseDateTime(guildInfo.getTimeCreated()), true)
					.setDescription(guildInfo.getDescription())
					.setThumbnail(guildInfo.getIconURL())
					.setColor(Constants.IGSQ_PURPLE)
					.build()).queue();
		}

	}
}
