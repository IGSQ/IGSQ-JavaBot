package org.igsq.igsqbot.entities.command;

public enum CommandFlag
{
	GUILD_ONLY(false),
	DEVELOPER_ONLY(false),
	AUTO_DELETE_MESSAGE(false),
	BLACKLIST_BYPASS(false);

	private final boolean defaultValue;

	CommandFlag(boolean defaultValue)
	{
		this.defaultValue = defaultValue;
	}

	public boolean getDefaultValue()
	{
		return defaultValue;
	}
}
