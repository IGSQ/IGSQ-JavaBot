package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Uwu_Command
{
	private final MessageChannel channel;
	private final User author;
	private final String[] args;

	public Uwu_Command(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.args = Common.depend(event.getMessage().getContentRaw().split(" ", 2), 0);

		uwuQuery();
	}

	private void uwuQuery()
	{
		if(!author.isBot()) uwu();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}

	private void uwu()
	{
		StringBuilder sentence = new StringBuilder();
		List<String> chars = Arrays.stream(args[0].split("")).collect(Collectors.toList());

		Collections.replaceAll(chars, "r", "w");
		Collections.replaceAll(chars, "o", "wo");
		Collections.replaceAll(chars, "l", "w");
		Collections.replaceAll(chars, "a", "aw");
		Collections.replaceAll(chars, "i", "iw");

		chars.forEach(sentence::append);
		new EmbedGenerator(channel).text(sentence.toString()).footer("This sentence was UwU'd by: " + author.getAsTag()).color(Color.PINK).send();
	}
}
