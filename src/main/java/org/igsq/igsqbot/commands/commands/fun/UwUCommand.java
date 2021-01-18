package org.igsq.igsqbot.commands.commands.fun;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;

@SuppressWarnings("unused")
public class UwUCommand extends Command
{
	public UwUCommand()
	{
		super("UwU", "UwU's the specified sentence.", "[text]");
		addAliases("uwu", "uwufy", "owo");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsEmpty(ctx);
		CommandChecks.argsEmbedCompatible(ctx);

		List<String> chars = Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList());
		MessageChannel channel = ctx.getChannel();
		StringBuilder finalSentence = new StringBuilder();
		User author = ctx.getAuthor();

		for(String selectedChar : chars)
		{
			switch(selectedChar)
			{
				case "r", "l" -> finalSentence.append("w");
				case "o" -> finalSentence.append("wo");
				case "a" -> finalSentence.append("aw");
				case "i" -> finalSentence.append("iw");
				default -> finalSentence.append(selectedChar);
			}
		}

		channel.sendMessage(new EmbedBuilder()
				.setDescription(finalSentence.toString())
				.setColor(Constants.IGSQ_PURPLE)
				.setFooter("This sentence was UwU'd by: " + author.getAsTag())
				.build()).queue();

	}
}
