package org.igsq.igsqbot.entities;

import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.List;

public abstract class NewCommand
{
    private final NewCommand parent;
    private final String name;
    private final String description;
    private final String syntax;
    private final List<NewCommand> children;
    private final List<String> aliases;
    private final List<Module> requiredModules;
    private final List<Permission> requiredPermissions;
    private boolean isGuildOnly;
    private boolean isDisabled;
    private boolean isDeveloperOnly;
    private long cooldown;

    protected NewCommand(NewCommand parent, String name, String description, String syntax)
    {
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.children = new ArrayList<>();
        this.aliases = new ArrayList<>();
        this.isGuildOnly = false;
        this.isDisabled = false;
        this.requiredModules = new ArrayList<>();
        this.requiredPermissions = new ArrayList<>();
        this.isDeveloperOnly = false;
        this.cooldown = 0;
    }

    protected NewCommand(String name, String description, String syntax)
    {
        this.parent = null;
        this.name = name;
        this.description = description;
        this.syntax = syntax;
        this.children = new ArrayList<>();
        this.aliases = new ArrayList<>();
        this.isGuildOnly = false;
        this.isDisabled = false;
        this.requiredModules = new ArrayList<>();
        this.requiredPermissions = new ArrayList<>();
        this.isDeveloperOnly = false;
        this.cooldown = 0;
    }

    public void process(List<String> args, CommandContext ctx)
    {
        if(!ctx.hasPermission(requiredPermissions))
        {

        }
        else if(isGuildOnly && !ctx.isFromGuild())
        {

        }
        else if(isDeveloperOnly && !ctx.isDeveloper())
        {

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

    public NewCommand getParent()
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

    public List<NewCommand> getChildren()
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

    public void addChildren(NewCommand... children)
    {
        this.children.addAll(List.of(children));
    }

    public void addAliases(String... aliases)
    {
        this.aliases.addAll(List.of(aliases));
    }

    public void addModules(Module... modules)
    {
        this.requiredModules.addAll(List.of(modules));
    }

    public List<Module> getRequiredModules()
    {
        return requiredModules;
    }

    public List<Permission> getRequiredPermissions()
    {
        return requiredPermissions;
    }

    public void addPermissions(Permission... permissions)
    {
        this.requiredPermissions.addAll(List.of(permissions));
    }
}
