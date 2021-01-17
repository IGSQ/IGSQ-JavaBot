package org.igsq.igsqbot;

import javax.security.auth.login.LoginException;

public class Main
{
	public static void main(String[] args)
	{
		IGSQBot bot = new IGSQBot();
		try
		{
			bot.getConfig();
			bot.build();
		}
		catch(LoginException exception)
		{
			bot.getLogger().error("The provided token was invalid, please ensure you put a valid token in bot.cfg");
			System.exit(1);
		}
		catch(IllegalArgumentException exception)
		{
			bot.getLogger().error("A provided value was invalid, please double check the values in bot.cfg");
			System.exit(1);
		}
		catch(Exception exception)
		{
			bot.getLogger().error("An unhandled exception occurred", exception);
			System.exit(1);
		}
	}
}
