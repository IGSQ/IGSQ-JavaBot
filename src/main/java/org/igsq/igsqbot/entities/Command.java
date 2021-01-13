package org.igsq.igsqbot.entities;

import java.util.List;

public abstract class Command
{
	private boolean isDisabled = false;

	public abstract void execute(final List<String> args, CommandContext ctx);

	public abstract String getName();

	public abstract List<String> getAliases();

	public abstract String getDescription();

	public abstract String getSyntax();

	public abstract boolean canExecute(CommandContext ctx);

	public abstract boolean isGuildOnly();

	public abstract long getCooldown();

	public boolean isDisabled()
	{
		return isDisabled;
	}

	public void setDisabled(boolean state)
	{
		isDisabled = state;
	}
}
