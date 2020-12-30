package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.stream.Collectors;

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
			return (arg.startsWith("<@!") && arg.endsWith(">")) || (arg.startsWith("<@") && arg.endsWith(">"));
		}
		catch(Exception exception)
		{
			return false;
		}
	}

	public static List<String> getRoleIds(Member member)
	{
		return member.getRoles().stream().map(Role::getId).collect(Collectors.toList());
	}

	public static boolean basicPermCheck(Member user, TextChannel channel)
	{
		return user.hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ, Permission.VIEW_CHANNEL, Permission.MESSAGE_EMBED_LINKS);
	}
}
