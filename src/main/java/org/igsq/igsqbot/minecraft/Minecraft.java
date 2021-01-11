package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.IGSQBot;

public class Minecraft
{
	private final IGSQBot igsqBot;
	private DatabaseHandler databaseHandler;
	private MinecraftSync sync;

	public Minecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		start();
	}

	public void start()
	{
		this.sync = new MinecraftSync(this);
		sync.start();
	}

	public DatabaseHandler getDatabaseHandler()
	{
		if(databaseHandler == null)
		{
			databaseHandler = new DatabaseHandler(igsqBot);
		}
		return databaseHandler;
	}

	public IGSQBot getIGSQBot()
	{
		return igsqBot;
	}

	public void close()
	{
		sync.close();
		databaseHandler.close();
	}
}
