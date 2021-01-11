package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.entities.Message;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Arrays;
import java.util.List;

public class VerificationCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		Message message = ctx.getMessage();

		if(args.size() != 1 || message.getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
	}

	@Override
	public String getName()
	{
		return "Verify";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("verify", "v", "accept");
	}

	@Override
	public String getDescription()
	{
		return "Verifies the specified user into the server";
	}

	@Override
	public String getSyntax()
	{
		return "[user]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}

}
