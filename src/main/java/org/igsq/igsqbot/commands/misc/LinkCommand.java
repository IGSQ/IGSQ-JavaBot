package org.igsq.igsqbot.commands.misc;

import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;

import java.util.Arrays;
import java.util.List;

public class LinkCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			String action = args.get(0);

			if(action.equalsIgnoreCase("add"))
			{
				addLink(args.get(1), ctx);
			}
			else if(action.equalsIgnoreCase("remove"))
			{
				removeLink(args.get(1), ctx);
			}
			else if(action.equalsIgnoreCase("show"))
			{
				showLink(args.get(1), ctx);
			}
			else
			{
				EmbedUtils.sendSyntaxError(ctx);
			}
		}
	}

	private void addLink(String arg, CommandContext ctx)
	{

	}

	private void removeLink(String arg, CommandContext ctx)
	{

	}

	private void showLink(String arg, CommandContext ctx)
	{
		new Parser(arg, ctx).parseAsUser(user ->
		{

		});
	}

	@Override
	public String getName()
	{
		return "Link";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("link", "mclink", "minecraft");
	}

	@Override
	public String getDescription()
	{
		return "Controls Minecraft links.";
	}

	@Override
	public String getSyntax()
	{
		return "[add|remove][mcName] | [show][user]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.getIGSQBot().getMinecraft().getDatabaseHandler().isOnline();
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}

	
