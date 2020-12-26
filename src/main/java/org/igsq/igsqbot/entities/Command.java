package org.igsq.igsqbot.entities;

import net.dv8tion.jda.api.Permission;

import java.util.List;

public abstract class Command
{
	private boolean isDisabled = false;

	public abstract void execute(final List<String> args, CommandContext ctx);

	public abstract String getName();

	public abstract List<String> getAliases();

	public abstract String getDescription();

	public abstract String getSyntax();

	public abstract List<Permission> getPermissions();

	public abstract boolean isRequiresGuild();

	public abstract int getCooldown();

	public boolean getDisabled()
	{
		return isDisabled;
	}

	public void setDisabled(boolean state)
	{
		isDisabled = state;
	}
}
