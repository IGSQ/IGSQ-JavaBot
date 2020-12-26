package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ModuleCommand extends Command
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCommand.class);

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(ArrayUtils.isValueInArray(this.getAliases().toArray(), args.get(1).toLowerCase()))
		{
			EmbedUtils.sendError(channel, "You cannot disable that command.");
		}
		else if(args.get(0).equalsIgnoreCase("enable"))
		{
			enableModule(args.get(1), channel);
		}
		else if(args.get(0).equalsIgnoreCase("disable"))
		{
			disableModule(args.get(1), channel);
		}
		else
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}

	}

	@Override
	public String getName()
	{
		return "Module";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("module", "command");
	}

	@Override
	public String getDescription()
	{
		return "Disables / Enables the specified module.";
	}

	@Override
	public String getSyntax()
	{
		return "[enable|disable] [module]";
	}

	@Override
	public List<Permission> getPermissions()
	{
		return Collections.singletonList(Permission.ADMINISTRATOR);
	}

	@Override
	public boolean isRequiresGuild()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}

	private void enableModule(String moduleName, MessageChannel channel)
	{
		Command cmd = CommandHandler.COMMAND_MAP.get(moduleName);
		if(cmd == null)
		{
			EmbedUtils.sendError(channel, "The specified module: `" + moduleName + "` was not found");
		}
		else
		{
			cmd.setDisabled(false);
			EmbedUtils.sendSuccess(channel, "Enabled module: `" + cmd.getName() + "`.");
			LOGGER.warn("Module " + cmd.getName() + " was enabled.");
		}
	}

	private void disableModule(String moduleName, MessageChannel channel)
	{
		Command cmd = CommandHandler.COMMAND_MAP.get(moduleName);
		if(cmd == null)
		{
			EmbedUtils.sendError(channel, "The specified module: `" + moduleName + "` was not found");
		}
		else
		{
			cmd.setDisabled(true);
			EmbedUtils.sendSuccess(channel, "Disabled module: `" + cmd.getName() + "`.");
			LOGGER.warn("Module " + cmd.getName() + " was disabled.");
		}
	}
}
