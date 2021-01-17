package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.IGSQBot;

public class Minecraft
{
	private final IGSQBot igsqBot;
	private final DatabaseHandler databaseHandler;
	private MinecraftSync sync;
	private MinecraftTwoFA twoFA;

	public Minecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		this.databaseHandler = new DatabaseHandler(igsqBot);
		start();
	}

	public void start()
	{
		this.sync = new MinecraftSync(this);
		this.twoFA = new MinecraftTwoFA(this);
		sync.start();
		twoFA.start();
	}

	public DatabaseHandler getDatabaseHandler()
	{
		return databaseHandler;
	}

	public IGSQBot getIGSQBot()
	{
		return igsqBot;
	}

	public void close()
	{
		sync.close();
		twoFA.close();
		databaseHandler.close();
	}
}
