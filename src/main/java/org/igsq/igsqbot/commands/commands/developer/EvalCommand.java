package org.igsq.igsqbot.commands.commands.developer;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.CommandChecks;

@SuppressWarnings("unused")

public class EvalCommand extends Command
{
	private static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("groovy");
	private static final List<String> DEFAULT_IMPORTS = List.of("net.dv8tion.jda.api.entities.impl", "net.dv8tion.jda.api.managers", "net.dv8tion.jda.api.entities", "net.dv8tion.jda.api",
			"java.io", "java.math", "java.util", "java.util.concurrent", "java.time", "java.util.stream");

    public EvalCommand()
    {
        super("Eval", "Evaluates Java code", "[code]");
        addFlags(CommandFlag.GUILD_ONLY, CommandFlag.DEVELOPER_ONLY);
        addAliases("eval", "evaluate", "code");
    }

    @Override
    public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
    {
		if(CommandChecks.argsEmpty(ctx, failure)) return;

		Object out;
		Color color = Color.GREEN;
		String status = "Success";

		if(ctx.isFromGuild())
		{
			SCRIPT_ENGINE.put("guild", ctx.getGuild());
			SCRIPT_ENGINE.put("member", ctx.getMember());
		}

		SCRIPT_ENGINE.put("ctx", ctx);
		SCRIPT_ENGINE.put("message", ctx.getMessage());
		SCRIPT_ENGINE.put("channel", ctx.getChannel());
		SCRIPT_ENGINE.put("args", ctx.getArgs());
		SCRIPT_ENGINE.put("jda", ctx.getJDA());
		SCRIPT_ENGINE.put("author", ctx.getAuthor());

		StringBuilder imports = new StringBuilder();
		DEFAULT_IMPORTS.forEach(imp -> imports.append("import ").append(imp).append(".*; "));
		String code = String.join(" ", ctx.getArgs());
		long start = System.currentTimeMillis();

		try
		{
			out = SCRIPT_ENGINE.eval(imports + code);
		}
		catch(Exception exception)
		{
			out = exception.getMessage();
			color = Color.RED;
			status = "Failed";
		}

		ctx.sendMessage(new EmbedBuilder()
				.setTitle("Evaluated Result")
				.setColor(color)
				.addField("Status:", status, true)
				.addField("Duration:", (System.currentTimeMillis() - start) + "ms", true)
				.addField("Code:", "```java\n" + code + "\n```", false)
				.addField("Result:", out == null ? "" : out.toString(), false));
	}
}

