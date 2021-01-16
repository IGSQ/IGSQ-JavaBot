package org.igsq.igsqbot.handlers;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.NewCommand;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.util.EmbedUtils;

public class CommandHandler
{
    public static final String COMMAND_PACKAGE = "org.igsq.igsqbot.commands2.commands";

    private final Map<String, NewCommand> commandMap;
    private final ClassGraph classGraph = new ClassGraph().acceptPackages(COMMAND_PACKAGE);
    private final ExecutorService commandExecutor = Executors.newFixedThreadPool(5);
    private final IGSQBot igsqBot;

    public CommandHandler(IGSQBot igsqBot)
    {
        this.igsqBot = igsqBot;
        commandMap = loadCommands();
    }

    private Map<String, NewCommand> loadCommands()
    {
        Map<String, NewCommand> COMMANDS = new HashMap<>();
        try (ScanResult result = classGraph.scan())
        {
            for (ClassInfo cls : result.getAllClasses())
            {
                Constructor<?>[] constructors = cls.loadClass().getDeclaredConstructors();
                if (constructors.length == 0)
                {
                    igsqBot.getLogger().warn("No valid constructors found for Command class (" + cls.getSimpleName() + ")!");
                    continue;
                }
                if (constructors[0].getParameterCount() > 0)
                {
                    continue;
                }
                Object instance = constructors[0].newInstance();
                if (!(instance instanceof NewCommand))
                {
                    igsqBot.getLogger().warn("None Command class (" + cls.getSimpleName() + ") found in commands package!");
                    continue;
                }
                NewCommand cmd = (NewCommand) instance;
                COMMANDS.put(cmd.getName(), cmd);
                for (String alias : cmd.getAliases()) COMMANDS.put(alias, cmd);
            }
        }
        catch (Exception exception)
        {
            new ErrorHandler(exception);
            System.exit(1);
        }
        return Collections.unmodifiableMap(COMMANDS);
    }

    public Map<String, NewCommand> getCommandMap()
    {
        return commandMap;
    }

    public void handle(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot() || event.isWebhookMessage())
        {
            return;
        }

        Message referencedMessage = event.getMessage().getReferencedMessage();

        if (referencedMessage != null && referencedMessage.getAuthor().equals(igsqBot.getSelfUser()))
        {
            return;
        }

        List<String> args = Arrays.stream(event.getMessage().getContentRaw().split("\\s+")).collect(Collectors.toList());
        String messageContent = event.getMessage().getContentRaw();
        JDA jda = event.getJDA();
        MessageChannel channel = event.getChannel();
        String selfID = jda.getSelfUser().getId();
        String commandText;
        String content;
        NewCommand cmd;

        boolean startsWithId = messageContent.startsWith("<@" + selfID + ">") || messageContent.startsWith("<@!" + selfID + ">");
        boolean startWithIdSpaced = messageContent.startsWith("<@" + selfID + "> ") || messageContent.startsWith("<@!" + selfID + "> ");
        String idTrimmed = messageContent.substring(messageContent.indexOf(">") + 1).trim();

        if (event.isFromGuild())
        {
            Guild guild = event.getGuild();
            if (startsWithId)
            {
                content = idTrimmed;
            }
            else if (messageContent.startsWith(new GuildConfig(guild.getIdLong(), igsqBot).getPrefix()))
            {
                String prefix = new GuildConfig(guild.getIdLong(), igsqBot).getPrefix();
                content = messageContent.substring(prefix.length()).trim();
            }
            else
            {
                return;
            }
        }
        else
        {
            String prefix = Constants.DEFAULT_BOT_PREFIX;
            if (startsWithId)
            {
                content = idTrimmed;
            }
            else if (messageContent.startsWith(prefix))
            {
                content = messageContent.substring(prefix.length()).trim();
            }
            else
            {
                return;
            }
        }

        commandText = (content.contains(" ") ? content.substring(0, content.indexOf(' ')) : content).toLowerCase();
        if (!commandText.isEmpty())
        {
            cmd = commandMap.get(commandText.toLowerCase());
            if (cmd == null)
            {
                EmbedUtils.sendError(channel, "The command `" + commandText + "` was not found.");
                return;
            }
            CommandContext ctx = new CommandContext(event, igsqBot, cmd);
            if (cmd.isDisabled())
            {
                EmbedUtils.sendDisabledError(ctx);
            }
            else if (cmd.isGuildOnly() && !event.isFromGuild())
            {
                EmbedUtils.sendError(channel, "This command requires execution in a server.");
            }
            else
            {
                args.remove(0);
                if (startWithIdSpaced)
                {
                    args.remove(0);
                }

                if (args.isEmpty())
                {
                    cmd.run(args, ctx);
                    return;
                }
                String subCommand = args.get(0);
                for (NewCommand child : cmd.getChildren())
                {
                    if (subCommand.equalsIgnoreCase(child.getName()))
                    {
                        args.remove(0);
                        child.run(args, new CommandContext(event, igsqBot, child));
                        return;
                    }
                }
                cmd.run(args, ctx);
            }
        }
    }

    public void close()
    {
        commandExecutor.shutdown();
    }
}
