package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.handlers.CommandHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ModuleCommand extends Command
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCommand.class);

	public ModuleCommand()
	{
		super("Module", new String[]{"module", "command"}, "Disables / Enables the specified module.", "[enable|disable] [module]", new Permission[]{Permission.ADMINISTRATOR},false, 0);
	}
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		if(args.size() != 2)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(ArrayUtils.isValueInArray(this.getAliases(), args.get(1).toLowerCase()))
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
			EmbedUtils.sendSuccess(channel, "Enabled module: `" + cmd.getInvoke() + "`.");
			LOGGER.warn("Module " + cmd.getInvoke() + " was enabled.");
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
			EmbedUtils.sendSuccess(channel, "Disabled module: `" + cmd.getInvoke() + "`.");
			LOGGER.warn("Module " + cmd.getInvoke() + " was disabled.");
		}
	}
}
