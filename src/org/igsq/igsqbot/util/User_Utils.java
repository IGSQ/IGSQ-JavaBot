package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.handlers.ErrorHandler;

public class User_Utils
{
	private User_Utils()
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
			user = Common.getJda().retrieveUserById(id).complete();
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

	public static String getRoleAsMention(String id)
	{
		return "<@&" + id + ">";
	}
}
