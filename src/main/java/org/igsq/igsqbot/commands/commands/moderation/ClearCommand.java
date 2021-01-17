package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import java.util.OptionalInt;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.CommandFlag;
import org.igsq.igsqbot.entities.cache.MessageCache;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

public class ClearCommand extends Command
{
	public ClearCommand()
	{
		super("Clear", "Clears messages from the current channel", "[amount {50}]");
		addAliases("clear", "purge");
		addPermissions(Permission.MESSAGE_MANAGE);
		addCooldown(10000);
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();
		Member member = ctx.getMember();
		Guild guild = ctx.getGuild();

		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
			return;
		}

		OptionalInt amount = new Parser(args.get(0), ctx).parseAsUnsignedInt();

		if(amount.isPresent())
		{
			if(amount.getAsInt() > 51)
			{
				EmbedUtils.sendSyntaxError(ctx);
			}
			else if(CooldownHandler.isOnCooldown(member, this))
			{
				//Do nothing
			}
			else
			{
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
}
