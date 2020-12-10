package org.igsq.igsqbot.objects;

import net.dv8tion.jda.api.Permission;

public abstract class Command
{
	private final String invoke;
	private final String[] aliases;
	private final String description;
	private final Permission[] requiredPermissions;
	private final int cooldown;
	private final boolean requiresGuild;

	protected Command(final String invoke, final String[] aliases, final String description, final Permission[] requiredPermissions, boolean requiresGuild, int cooldown)
	{
		this.invoke = invoke;
		this.aliases = aliases;
		this.description = description;
		this.requiredPermissions = requiredPermissions;
		this.requiresGuild = requiresGuild;
		this.cooldown = cooldown;
	}

	public abstract void execute(final String[] args, Context ctx);

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

	public boolean isRequiresGuild()
	{
		return requiresGuild;
	}
}
