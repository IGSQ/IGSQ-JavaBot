package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.IGSQBot;

public class Minecraft
{
	private final IGSQBot igsqBot;
	private DatabaseHandler databaseHandler;

	public Minecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
	}

	public DatabaseHandler getDatabaseHandler()
	{
		if(databaseHandler == null)
		{
			databaseHandler = new DatabaseHandler(igsqBot);
		}
		return databaseHandler;
	}

	public void close()
	{
		igsqBot.getTaskHandler().cancelTask("minecraftClean", false);
		igsqBot.getTaskHandler().cancelTask("minecraftSync", false);
		igsqBot.getTaskHandler().cancelTask("2FATask", false);
	}
}
