package main.java.org.igsq.igsqbot.util;

import main.java.org.igsq.igsqbot.IGSQBot;
import main.java.org.igsq.igsqbot.handlers.ErrorHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

public class UserUtils
{
	private UserUtils()
	{
		//Overrides the default, public constructor
	}
	public static boolean isUserMention(String arg)
	{
		try
		{
			return arg.startsWith("<@!") && arg.endsWith(">")/* && arg.substring(3, 21).matches("[0-9]")*/;
		}
		catch(Exception exception)
		{
			return false;
		}
	}

	public static boolean isUserId(String arg)
	{
		try
		{
			return arg.substring(0, 18)/*.matches("[0-9]")*/ != null;
		}
		catch(Exception exception)
		{
			return false;
		}
	}

	public static String getUserIdFromMention(String arg)
	{
		if(isUserMention(arg)) return arg.substring(3, 21);
		else return null;
	}

	public static Member getMemberFromUser(User user, Guild guild)
	{
		Member member = null;
		try
		{
			member = guild.retrieveMember(user).complete();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return member;
	}

	public static User getUserFromMention(String arg)
	{
		if(isUserMention(arg)) return getUserById(arg.substring(3, 21));
		else if(isUserId(arg)) return getUserById(arg);
		else return null;
	}

	public static User getUserById(String id)
	{
		User user = null;
		try
		{
			user = IGSQBot.getJDA().retrieveUserById(id).complete();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return user;
	}

	public static Role getRoleFromMention(Guild guild, String arg)
	{
		if(isRoleMention(arg)) return guild.getRoleById(arg.substring(3, 21));
		else if(isRoleId(arg)) return guild.getRoleById(arg);
		else return null;
	}

	public static boolean isRoleId(String arg)
	{
		try
		{
			return arg.substring(0, 18)/*.matches("[0-9]")*/ != null;
		}
		catch(Exception exception)
		{
			return false;
		}
	}

	public static boolean isRoleMention(String arg)
	{
		try
		{
			return arg.startsWith("<@&") && arg.endsWith(">")/* && arg.substring(3, 21).matches("[0-9]")*/;
		}
		catch(Exception exception)
		{
			return false;
		}
	}

	public static boolean basicPermCheck(Member user, GuildChannel channel)
	{
		return user.hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ, Permission.VIEW_CHANNEL);
	}

	public static String getRoleAsMention(String id)
	{
		return "<@&" + id + ">";
	}
}
