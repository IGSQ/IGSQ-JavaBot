package org.igsq.igsqbot.entities.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.exception.*;
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

	public void process(CommandEvent cmd)
	{
		if(isDisabled() || hasFlag(CommandFlag.DISABLED))
		{
			EmbedUtils.sendDisabledError(cmd);
		}
		else if(hasFlag(CommandFlag.GUILD_ONLY) && !cmd.isFromGuild())
		{
			cmd.replyError("This command must be executed in a server.");
		}
		else if(!getMemberRequiredPermissions().isEmpty() && !cmd.memberPermissionCheck(getMemberRequiredPermissions()))
		{
			EmbedUtils.sendMemberPermissionError(cmd);
		}
		else if(!getSelfRequiredPermissions().isEmpty() && !cmd.selfPermissionCheck(getSelfRequiredPermissions()))
		{
			EmbedUtils.sendSelfPermissionError(cmd);
		}
		else if(hasFlag(CommandFlag.DEVELOPER_ONLY) && !cmd.isDeveloper())
		{
			cmd.replyError("This command is for developers only.");
		}
		else
		{
			if(hasFlag(CommandFlag.AUTO_DELETE_MESSAGE) && cmd.selfPermissionCheck(Permission.MESSAGE_MANAGE))
			{
				cmd.getMessage().delete().queue();
			}

			run(cmd.getArgs(), cmd, exception ->
			{
				if(exception instanceof CommandCooldownException)
				{
					cmd.replyError(cmd.getAuthor().getAsMention() + " is on cooldown from command `" + getName() + "`");
				}
				else if(exception instanceof CommandResultException)
				{
					cmd.replyError("An error occurred. " + exception.getText());
				}
				else if(exception instanceof CommandInputException)
				{
					cmd.replyError(exception.getText());
				}
				else if(exception instanceof CommandSyntaxException)
				{
					EmbedUtils.sendSyntaxError(cmd);
				}
				else if(exception instanceof CommandHierarchyException)
				{
					cmd.replyError("A hierarchy error occurred when trying to process command `" + getName() + "`");
				}
				else if(exception instanceof CommandUserPermissionException)
				{
					EmbedUtils.sendMemberPermissionError(cmd);
				}
				else if(exception instanceof MissingConfigurationException)
				{
					cmd.replyError("`" + exception.getText() + "` is not setup.");
				}
				else
				{
					cmd.replyError("An unhandled error occurred " + exception.getText());
				}
			});
		}
	}

	public boolean hasChildren()
	{
		return !getChildren().isEmpty();
	}

	public abstract void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure);

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
