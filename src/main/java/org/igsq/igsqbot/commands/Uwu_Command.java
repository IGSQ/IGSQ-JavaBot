package main.java.org.igsq.igsqbot.commands;

import main.java.org.igsq.igsqbot.objects.Command;
import main.java.org.igsq.igsqbot.objects.Context;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.util.ArrayUtils;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Uwu_Command extends Command
{
	public Uwu_Command()
	{
		super("uwu", new String[]{"uwufy"}, "UwU's the specified sentence","[text]", new Permission[]{}, false,0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		final List<String> chars = Arrays.stream(ArrayUtils.arrayCompile(args, " ").split("")).collect(Collectors.toList());
		final StringBuilder finalSentence = new StringBuilder();
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();

		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel,this);
			return;
		}

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
