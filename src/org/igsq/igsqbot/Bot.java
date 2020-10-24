package org.igsq.igsqbot;

import org.igsq.igsqbot.commands.Main_Command;

import net.dv8tion.jda.api.JDABuilder;

public class Bot
{
	public static void main(String[] args)
	{
		try 
		{
			Common.jdaBuilder = JDABuilder.createDefault(args[0]);
			Common.jdaBuilder.addEventListeners(new Main_Command());
			
			Common.jda = Common.jdaBuilder.build();
			Common.self = Common.jda.getSelfUser();
			
			Common.jda.awaitReady();
			
		}
		catch(Exception exception)
		{
			System.err.println("GGEZGN");
		}
	}
}
