package org.igsq.igsqbot.commands.fun;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UwUCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		List<String> chars = Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList());
		MessageChannel channel = ctx.getChannel();
		StringBuilder finalSentence = new StringBuilder();
		User author = ctx.getAuthor();

		if(args.isEmpty() || CommandUtils.isArgsEmbedCompatible(args))
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else if(ctx.getChannelType().equals(ChannelType.TEXT) && !UserUtils.basicPermCheck(ctx.getGuild().getSelfMember(), (TextChannel) channel))
		{
			EmbedUtils.sendPermissionError(ctx);
		}
		else
		{
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

	@Override
	public String getName()
	{
		return "UwU";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("uwu", "uwufy", "owo");
	}

	@Override
	public String getDescription()
	{
		return "UwU's the specified sentence";
	}

	@Override
	public String getSyntax()
	{
		return "[text]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
