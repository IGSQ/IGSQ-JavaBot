package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UwuCommand extends Command
{
	public UwuCommand()
	{
		super("UwU", new String[]{"uwu", "uwufy", "owo"}, "UwU's the specified sentence", "[text]", new Permission[]{}, false, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final List<String> chars = Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList());
		final MessageChannel channel = ctx.getChannel();
		final StringBuilder finalSentence = new StringBuilder();
		final User author = ctx.getAuthor();

		if(args.isEmpty() || CommandUtils.isCommandTooLarge(args))
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(ctx.getChannelType().equals(ChannelType.TEXT) && !UserUtils.basicPermCheck(ctx.getGuild().getSelfMember(), (TextChannel) channel))
		{
			EmbedUtils.sendPermissionError(channel, this);
		}
		else
		{
			for(String selectedChar : chars)
			{
				switch(selectedChar)
				{
					case "r" -> finalSentence.append("w");
					case "o" -> finalSentence.append("wo");
					case "l" -> finalSentence.append("w");
					case "a" -> finalSentence.append("aw");
					case "i" -> finalSentence.append("iw");
					default -> finalSentence.append(selectedChar);
				}
			}
			new EmbedGenerator(channel)
					.text(finalSentence.toString())
					.footer("This sentence was UwU'd by: " + author.getAsTag())
					.color(EmbedUtils.IGSQ_PURPLE).send();
		}
	}
}
