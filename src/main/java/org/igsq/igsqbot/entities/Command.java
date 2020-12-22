package org.igsq.igsqbot.entities;

import net.dv8tion.jda.api.Permission;

import java.util.List;

public abstract class Command
{
	private final String invoke;
	private final String[] aliases;
	private final String description;
	private final Permission[] requiredPermissions;
	private final int cooldown;
	private final boolean requiresGuild;
	private final String syntax;
	private boolean disabled = false;

	protected Command(final String name, final String[] aliases, final String description, final String syntax, final Permission[] requiredPermissions, boolean requiresGuild, int cooldown)
	{
		this.invoke = name;
		this.aliases = aliases;
		this.description = description;
		this.syntax = syntax;
		this.requiredPermissions = requiredPermissions;
		this.requiresGuild = requiresGuild;
		this.cooldown = cooldown;
	}

	public abstract void execute(final List<String> args, CommandContext ctx);

	public String getInvoke()
	{
		return invoke;
	}

	public String[] getAliases()
	{
		return aliases;
	}

	public String getDescription()
	{
		return description;
	}

	public Permission[] getRequiredPermissions()
	{
		return requiredPermissions;
	}

	public int getCooldown()
	{
		return cooldown;
	}

	public String getSyntax()
	{
		return syntax;
	}

	public boolean isRequiresGuild()
	{
		return requiresGuild;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}
}
