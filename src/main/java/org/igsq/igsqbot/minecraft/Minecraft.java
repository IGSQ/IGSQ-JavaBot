package org.igsq.igsqbot.minecraft;

import org.igsq.igsqbot.IGSQBot;

public class Minecraft
{
	private final IGSQBot igsqBot;

	public Minecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		new SyncMinecraft(igsqBot);
		new CleanMinecraft(igsqBot);
		new TwoFAMinecraft(igsqBot);
	}

	public void close()
	{
		igsqBot.getTaskHandler().cancelTask("minecraftClean");
		igsqBot.getTaskHandler().cancelTask("minecraftSync");
		igsqBot.getTaskHandler().cancelTask("2FATask");
	}
}
