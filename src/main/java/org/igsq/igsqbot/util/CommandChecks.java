package org.igsq.igsqbot.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.exception.ConfigurationException;
import org.igsq.igsqbot.entities.exception.IllegalLengthException;
import org.igsq.igsqbot.entities.exception.SyntaxException;

public class CommandChecks
{
	private CommandChecks()
	{
		//Overrides the default, public, constructor
	}

	public static void channelConfigured(MessageChannel channel, String name)
	{
		if(channel == null)
		{
			throw new ConfigurationException(name);
		}
	}

	public static void roleConfigured(Role role, String name)
	{
		if(role == null)
		{
			throw new ConfigurationException(name);
		}
	}
	public static void stringMatches(String input, String regex, CommandContext ctx)
	{
		if(!input.matches(regex))
		{
			throw new SyntaxException(ctx);
		}
	}
	public static void stringIsURL(String url, CommandContext ctx)
	{
		try
		{
			URL obj = new URL(url);
			obj.toURI();
		}
		catch(Exception exception)
		{
			throw new SyntaxException(ctx);
		}
	}

	public static void argsEmpty(CommandContext ctx)
	{
		if(ctx.getArgs().isEmpty())
		{
			throw new SyntaxException(ctx);
		}
	}

	public static void argsSizeExceeds(CommandContext ctx, int size)
	{
		if(ctx.getArgs().size() > size)
		{
			throw new SyntaxException(ctx);
		}
	}

	public static void argsSizeSubceeds(CommandContext ctx, int size)
	{
		if(ctx.getArgs().size() < size)
		{
			throw new SyntaxException(ctx);
		}
	}

	public static void argsSizeSubceeds(List<String> args, CommandContext ctx, int size)
	{
		if(args.size() < size)
		{
			throw new SyntaxException(ctx);
		}
	}

	public static void argsSizeMatches(CommandContext ctx, int size)
	{
		if(ctx.getArgs().size() != size)
		{
			throw new SyntaxException(ctx);
		}
	}

	public static void argsEmbedCompatible(CommandContext ctx)
	{
		List<Character> chars = new ArrayList<>();
		ctx.getArgs().stream().map(arg -> arg.split("")).forEach(
		words ->
		{
			for(String word : words)
			{
				for(char character : word.toCharArray())
				{
					chars.add(character);
				}
			}
		});
		if(chars.size() > EmbedUtils.CHARACTER_LIMIT)
		{
			throw new IllegalLengthException(ctx);
		}
	}
}
