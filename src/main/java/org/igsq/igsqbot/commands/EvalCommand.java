package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.List;

public class EvalCommand extends Command
{
	public EvalCommand()
	{
		super("Eval", new String[]{"eval"}, "Evaluates the specifed JS code,", "[code]", new Permission[]{Permission.ADMINISTRATOR}, true, 0);
	}
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		StringBuilder imports = new StringBuilder();
		List<String> DEFAULT_IMPORTS = Arrays.asList("net.dv8tion.jda.api.entities.impl", "net.dv8tion.jda.api.managers", "net.dv8tion.jda.api.entities", "net.dv8tion.jda.api",
				"java.io", "java.math", "java.util", "java.util.concurrent", "java.time", "java.util.stream"
		);
		DEFAULT_IMPORTS.forEach(imp -> imports.append("import ").append(imp).append(".*; "));

		String toEval = imports + ArrayUtils.arrayCompile(args, "");
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");

		engine.put("ctx", ctx);
		engine.put("channel", ctx.getChannel());
		engine.put("api", ctx.getJDA());
		engine.put("jda", ctx.getJDA());
		engine.put("guild", ctx.getGuild());
		engine.put("user", ctx.getAuthor());

		try
		{
			EmbedUtils.sendSuccess(ctx.getChannel(), "Evaluated Successfully:\n```\n" + engine.eval(toEval) + "```");
		}
		catch(Exception exception)
		{
			EmbedUtils.sendError(ctx.getChannel(), "An exception occurred while evaluating.");
			new ErrorHandler(exception);
		}
	}
}
