package org.igsq.igsqbot.improvedcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Uwu_Command extends Command
{
	public Uwu_Command()
	{
		super("uwu", new String[]{"uwufy"}, "UwU's the specified sentence", new Permission[]{}, false,0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final StringBuilder sentence = new StringBuilder();
		final List<String> chars = Arrays.stream(args[0].split("")).collect(Collectors.toList());
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();

		Collections.replaceAll(chars, "r", "w");
		Collections.replaceAll(chars, "o", "wo");
		Collections.replaceAll(chars, "l", "w");
		Collections.replaceAll(chars, "a", "aw");
		Collections.replaceAll(chars, "i", "iw");

		chars.forEach(sentence::append);
		new EmbedGenerator(channel).text(sentence.toString())
				.footer("This sentence was UwU'd by: " + author.getAsTag())
				.color(Color.PINK).send();
	}
}
