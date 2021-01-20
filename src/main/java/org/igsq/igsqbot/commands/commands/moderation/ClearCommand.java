package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandCooldownException;
import org.igsq.igsqbot.entities.exception.CommandSyntaxException;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;

@SuppressWarnings("unused")
public class ClearCommand extends Command
{
	public ClearCommand()
	{
		super("Clear", "Clears messages from the current channel", "[amount {50}]");
		addAliases("clear", "purge");
		addMemberPermissions(Permission.MESSAGE_MANAGE);
		addSelfPermissions(Permission.MESSAGE_MANAGE);
		addCooldown(10000);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(ctx, failure)) return;

		MessageChannel channel = ctx.getChannel();
		Member member = ctx.getMember();
		Guild guild = ctx.getGuild();
		OptionalInt amount = new Parser(args.get(0), ctx).parseAsUnsignedInt();

		if(amount.isPresent())
		{
			if(amount.getAsInt() > 51)
			{
				failure.accept(new CommandSyntaxException(this));
				return;
			}

			if(CooldownHandler.isOnCooldown(member, this))
			{
				failure.accept(new CommandCooldownException(this));
				return;
			}

			channel.getIterableHistory().takeAsync(amount.getAsInt()).thenAccept(messages ->
			{
				CooldownHandler.addCooldown(member, this);
				channel.purgeMessages(messages);
				ctx.replySuccess("Deleted " + (messages.size()) + " messages");
				MessageCache cache = MessageCache.getCache(guild);
				messages.stream().filter(cache::isInCache).forEach(cache::remove);
			});
		}
	}
}
