package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.entities.exception.CommandUserPermissionException;
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
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
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
			failure.accept(new CommandInputException("Prefix was too long or contained spaces."));
			return;
		}

		if(!ctx.memberPermissionCheck(Permission.MANAGE_SERVER))
		{
			failure.accept(new CommandUserPermissionException(this));
			return;
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
			addMemberPermissions(Permission.MANAGE_SERVER);
		}

		@Override
		public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
		{
			GuildConfig guildConfig = new GuildConfig(ctx);
			ctx.replySuccess("Reset my prefix to `" + Constants.DEFAULT_BOT_PREFIX + "`");
			guildConfig.setPrefix(Constants.DEFAULT_BOT_PREFIX);
		}
	}
}