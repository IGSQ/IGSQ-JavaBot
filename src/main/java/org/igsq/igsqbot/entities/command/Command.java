package org.igsq.igsqbot.entities.command;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.ConfigurationException;
import org.igsq.igsqbot.entities.exception.IllegalLengthException;
import org.igsq.igsqbot.entities.exception.SyntaxException;
import org.igsq.igsqbot.util.BlacklistUtils;
import org.igsq.igsqbot.util.EmbedUtils;

public abstract class Command
{
	private final Command parent;
	private final String name;
	private final String description;
	private final String syntax;
	private final List<Command> children;
	private final List<String> aliases;
	private final List<Permission> memberRequiredPermissions;
	private final List<Permission> selfRequiredPermissions;
	private final List<CommandFlag> flags;
	private boolean isDisabled;
	private long cooldown;

	protected Command(Command parent, String name, String description, String syntax)
	{
		this.parent = parent;
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.children = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.isDisabled = false;
		this.memberRequiredPermissions = new ArrayList<>();
		this.selfRequiredPermissions = new ArrayList<>();
		this.cooldown = 0;
		this.flags = new ArrayList<>();
	}

	protected Command(String name, String description, String syntax)
	{
		this.parent = null;
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.children = new ArrayList<>();
		this.aliases = new ArrayList<>();
		this.isDisabled = false;
		this.memberRequiredPermissions = new ArrayList<>();
		this.selfRequiredPermissions = new ArrayList<>();
		this.cooldown = 0;
		this.flags = new ArrayList<>();
	}
	public void process(CommandContext ctx)
	{
		if(isDisabled())
		{
			EmbedUtils.sendDisabledError(ctx);
		}
		else if(ctx.isFromGuild() && BlacklistUtils.isBlacklistedPhrase(ctx.getEvent(), ctx.getIGSQBot()) && !hasFlag(CommandFlag.BLACKLIST_BYPASS))
		{
			ctx.replyError("Your message contained a blacklisted message.");
			if(ctx.getSelfMember().hasPermission((GuildChannel) ctx.getChannel(), Permission.MESSAGE_MANAGE))
			{
				ctx.getMessage().delete().queue();
			}
		}
		else if(hasFlag(CommandFlag.GUILD_ONLY) && !ctx.isFromGuild())
		{
			ctx.replyError("This command must be executed in a server.");
		}
		if(hasFlag(CommandFlag.AUTO_DELETE_MESSAGE) && ctx.getSelfMember().hasPermission((GuildChannel) ctx.getChannel(), Permission.MESSAGE_MANAGE))
		{
			ctx.getMessage().delete().queue();
		}
		else if(!getMemberRequiredPermissions().isEmpty() && !ctx.memberPermissionCheck(getMemberRequiredPermissions()))
		{
			EmbedUtils.sendMemberPermissionError(ctx);
		}
		else if(!getSelfRequiredPermissions().isEmpty() && !ctx.selfPermissionCheck(getSelfRequiredPermissions()))
		{
			EmbedUtils.sendSelfPermissionError(ctx);
		}
		else if(hasFlag(CommandFlag.DEVELOPER_ONLY) && !ctx.isDeveloper())
		{
			ctx.replyError("This command is for developers only.");
		}
		else
		{
			try
			{
				run(ctx.getArgs(), ctx);
				ctx.addSuccessReaction();
			}
			catch(IllegalLengthException exception)
			{
				ctx.replyError("The provided input was too long for an embed.");
				ctx.getMessage().delete().queue();
			}
			catch(SyntaxException exception)
			{
				EmbedUtils.sendSyntaxError(ctx);
			}
			catch(ConfigurationException exception)
			{
				ctx.replyError(exception.getText() + " is not setup.");
			}
			catch(CommandException exception)
			{
				ctx.replyError("An error occurred while executing command " + getName() + "\n" + exception.getText());
			}
		}
	}

	public boolean hasChildren()
	{
		return !getChildren().isEmpty();
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

	public void addChildren(Command... children)
	{
		this.children.addAll(List.of(children));
	}

	public void addAliases(String... aliases)
	{
		this.aliases.addAll(List.of(aliases));
	}

	public List<Permission> getMemberRequiredPermissions()
	{
		return memberRequiredPermissions;
	}

	public void addMemberPermissions(Permission... permissions)
	{
		this.memberRequiredPermissions.addAll(List.of(permissions));
	}

	public void addSelfPermissions(Permission... permissions)
	{
		this.selfRequiredPermissions.addAll(List.of(permissions));
	}

	public List<Permission> getSelfRequiredPermissions()
	{
		return selfRequiredPermissions;
	}

	public List<CommandFlag> getFlags()
	{
		return flags;
	}

	public void addFlags(CommandFlag... flags)
	{
		this.flags.addAll(List.of(flags));
	}

	public boolean hasFlag(CommandFlag flag)
	{
		return this.flags.contains(flag) || flag.getDefaultValue();
	}
}
