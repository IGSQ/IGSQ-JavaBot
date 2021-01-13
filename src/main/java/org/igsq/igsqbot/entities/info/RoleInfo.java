package org.igsq.igsqbot.entities.info;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.utils.concurrent.Task;

import java.awt.*;
import java.util.List;

public class RoleInfo
{
	private final Role role;

	public RoleInfo(Role role)
	{
		this.role = role;
	}

	public Task<List<Member>> getMembers()
	{
		return role.getGuild().findMembers(member -> member.getRoles().contains(role));
	}

	public String getAsMention()
	{
		return role.getAsMention();
	}

	public Color getColor()
	{
		return role.getColor();
	}
}
