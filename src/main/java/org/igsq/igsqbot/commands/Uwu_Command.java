package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Uwu_Command extends Command
{
	public Uwu_Command()
	{
		super("UwU", new String[]{"uwu", "uwufy", "owo"}, "UwU's the specified sentence","[text]", new Permission[]{}, false,0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final List<String> chars = Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList());
		final StringBuilder finalSentence = new StringBuilder();
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();

		if(args.isEmpty() || CommandUtils.isCommandTooLarge(args))
		{
			EmbedUtils.sendSyntaxError(channel,this);
		}
		else if(!ctx.getChannelType().equals(ChannelType.TEXT) && UserUtils.basicPermCheck(ctx.getGuild().getSelfMember(), (GuildChannel) channel))
		{
			EmbedUtils.sendPermissionError(channel, this);
		}
		else
		{
			Collections.replaceAll(chars, "r", "w");
			Collections.replaceAll(chars, "o", "wo");
			Collections.replaceAll(chars, "l", "w");
			Collections.replaceAll(chars, "a", "aw");
			Collections.replaceAll(chars, "i", "iw");

			chars.forEach(finalSentence::append);
			new EmbedGenerator(channel)
					.text(finalSentence.toString())
					.footer("This sentence was UwU'd by: " + author.getAsTag())
					.color(EmbedUtils.IGSQ_PURPLE).send();
		}
	}
}
