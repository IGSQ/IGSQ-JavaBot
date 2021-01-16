package org.igsq.igsqbot.entities;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.util.EmbedUtils;

public abstract class Command
{
	private final Command parent;
	private final String name;
	private final String description;
	private final String syntax;
	private final List<Command> children;
	private final List<String> aliases;
	private final List<Permission> requiredPermissions;
	private boolean autoDelete;
	private boolean isGuildOnly;
	private boolean isDisabled;
	private boolean isDeveloperOnly;
	private long cooldown;

	protected Command(Command parent, String name, String description, String syntax)
	{
		this.parent = parent;
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.children = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.isGuildOnly = false;
		this.isDisabled = false;
		this.requiredPermissions = new ArrayList<>();
		this.isDeveloperOnly = false;
		this.autoDelete = false;
		this.cooldown = 0;
	}

	protected Command(String name, String description, String syntax)
	{
		this.parent = null;
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.children = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.isGuildOnly = false;
		this.isDisabled = false;
		this.requiredPermissions = new ArrayList<>();
		this.isDeveloperOnly = false;
		this.autoDelete = false;
		this.cooldown = 0;
	}

	public void process(List<String> args, CommandContext ctx)
	{
		if(isDisabled())
		{
			EmbedUtils.sendDisabledError(ctx);
		}
		else if(!ctx.hasPermission(getRequiredPermissions()))
		{
			EmbedUtils.sendPermissionError(ctx);
		}
		else if(isGuildOnly() && !ctx.isFromGuild())
		{
			ctx.replyError("This command must be executed in a server.");
		}
		else if(isDeveloperOnly() && !ctx.isDeveloper())
		{
			ctx.replyError("This command is for developers only.");
		}
		else
		{
			run(args, ctx);
		}
	}

	public abstract void run(List<String> args, CommandContext ctx);

	public void addCooldown(long millis)
	{
		this.cooldown = millis;
	}

	public long getCooldown()
	{
		return cooldown;
	}

	public boolean isDisabled()
	{
		return isDisabled;
	}

	public void setDisabled(boolean disabled)
	{
		isDisabled = disabled;
	}

	public Command getParent()
	{
		return parent;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getSyntax()
	{
		return syntax;
	}

	public List<Command> getChildren()
	{
		return children;
	}

	public List<String> getAliases()
	{
		return aliases;
	}

	public boolean isGuildOnly()
	{
		return isGuildOnly;
	}

	public void guildOnly()
	{
		this.isGuildOnly = true;
	}

	public boolean isDeveloperOnly()
	{
		return isDeveloperOnly;
	}

	public void developerOnly()
	{
		this.isDeveloperOnly = true;
	}

	public void addChildren(Command... children)
	{
		this.children.addAll(List.of(children));
	}

	public void addAliases(String... aliases)
	{
		this.aliases.addAll(List.of(aliases));
	}

	public List<Permission> getRequiredPermissions()
	{
		return requiredPermissions;
	}

	public boolean isAutoDelete()
	{
		return autoDelete;
	}

	public void autoDelete()
	{
		autoDelete = true;
	}

	public void addPermissions(Permission... permissions)
	{
		this.requiredPermissions.addAll(List.of(permissions));
	}
}
