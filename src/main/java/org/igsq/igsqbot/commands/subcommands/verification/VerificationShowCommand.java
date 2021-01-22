package org.igsq.igsqbot.commands.subcommands.verification;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.VerificationUtils;

public class VerificationShowCommand extends Command
{
	public VerificationShowCommand(Command parent)
	{
		super(parent, "show", "Shows the configured phrases and roles.", "[none]");
		addFlags(CommandFlag.GUILD_ONLY);
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		Map<String, Long> phrases = VerificationUtils.getMappedPhrases(cmd.getGuild(), cmd.getIGSQBot());
		StringBuilder text = new StringBuilder();

		phrases.forEach(
				(phrase, role) -> text
						.append(StringUtils.getRoleAsMention(role))
						.append(" -> ")
						.append(phrase)
						.append("\n"));

		cmd.sendMessage(new EmbedBuilder()
				.setTitle("Aliases setup for " + cmd.getGuild().getName())
				.setDescription(text.length() == 0 ? "No aliases setup" : text.toString()));
	}
}
