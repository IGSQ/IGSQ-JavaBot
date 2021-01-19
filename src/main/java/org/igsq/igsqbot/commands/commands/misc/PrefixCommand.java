package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.exception.MemberPermissionException;
import org.igsq.igsqbot.util.EmbedUtils;

@SuppressWarnings("unused")
public class PrefixCommand extends Command
{
	public PrefixCommand()
	{
		super("Prefix", "Gets and sets the prefix for this server.", "<newPrefix {5}> / <reset> / <none>");
		addFlags(CommandFlag.GUILD_ONLY);
		addAliases("prefix");
		addChildren(new PrefixResetCommand(this));
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();
		GuildConfig guildConfig = new GuildConfig(ctx);

		if(args.isEmpty())
		{
			EmbedUtils.sendDeletingEmbed(channel, new EmbedBuilder()
					.setDescription("My prefix for this server is `" + guildConfig.getPrefix() + "`.")
					.setColor(Constants.IGSQ_PURPLE), 30000);
			return;
		}

		if(args.size() > 1 || args.get(0).length() > 5)
		{
			throw new IllegalArgumentException("Prefix was too long or contained spaces.");
		}

		if(!ctx.memberPermissionCheck(Permission.MANAGE_SERVER))
		{
			throw new MemberPermissionException(this);
		}

		guildConfig.setPrefix(args.get(0));
		ctx.replySuccess("My new prefix is `" + args.get(0) + "`");
	}

	public static class PrefixResetCommand extends Command
	{
		public PrefixResetCommand(Command parent)
		{
			super(parent, "reset", "Resets the prefix", "[none]");
			addFlags(CommandFlag.GUILD_ONLY);
		}

		@Override
		public void run(List<String> args, CommandContext ctx)
		{
			GuildConfig guildConfig = new GuildConfig(ctx);
			ctx.replySuccess("Reset my prefix to `" + Constants.DEFAULT_BOT_PREFIX + "`");
			guildConfig.setPrefix(Constants.DEFAULT_BOT_PREFIX);
		}
	}
}