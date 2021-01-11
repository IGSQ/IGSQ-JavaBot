package org.igsq.igsqbot.entities;

public enum Filename
{
	BOT("bot"),
	MINECRAFT("minecraft");

	private final String name;

	Filename(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
