package org.igsq.igsqbot.entities;

public enum CommandFlag
{
	GUILD_ONLY(false),
	DEVELOPER_ONLY(false),
	AUTO_DELETE_MESSAGE(false);

	public boolean getDefaultValue()
	{
		return defaultValue;
	}
	private final boolean defaultValue;
	CommandFlag(boolean defaultValue)
	{
		this.defaultValue = defaultValue;
	}
}
