package org.igsq.igsqbot.entities.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildChannel;
import org.igsq.igsqbot.entities.exception.*;
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

	public void process(CommandEvent ctx)
	{
		if(isDisabled() || hasFlag(CommandFlag.DISABLED))
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
			if(hasFlag(CommandFlag.AUTO_DELETE_MESSAGE) && ctx.selfPermissionCheck(Permission.MESSAGE_MANAGE))
			{
				ctx.getMessage().delete().queue();
			}
			run(ctx.getArgs(), ctx, exception ->
			{
				if(exception instanceof CommandCooldownException)
				{
					ctx.replyError(ctx.getAuthor().getAsMention() + " is on cooldown from command `" + getName() + "`");
				}
				else if(exception instanceof CommandResultException)
				{
					ctx.replyError("Something went wrong. " + exception.getText());
				}
				else if(exception instanceof CommandInputException)
				{
					ctx.replyError(exception.getText());
				}
				else if(exception instanceof CommandSyntaxException)
				{
					EmbedUtils.sendSyntaxError(ctx);
				}
				else if(exception instanceof CommandHierarchyException)
				{
					ctx.replyError("A hierarchy error occurred when trying to process command `" + getName() + "`");
				}
				else if(exception instanceof CommandUserPermissionException)
				{
					EmbedUtils.sendMemberPermissionError(ctx);
				}
				else if(exception instanceof MissingConfigurationException)
				{
					ctx.replyError("`" + exception.getText() + "` is not setup.");
				}
				else
				{
					ctx.replyError("An unhandled error occurred " + exception.getText());
				}
			});
		}
	}

	public boolean hasChildren()
	{
		return !getChildren().isEmpty();
	}

	public abstract void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure);

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
