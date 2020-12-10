package org.igsq.igsqbot.improvedcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.util.Command_Utils;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.Messaging;

import java.awt.*;

public class Poll_Command extends Command
{
	public Poll_Command()
	{
		super("poll", new String[]{}, "Starts a poll for users to vote in.", new Permission[]{}, false,0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final StringBuilder options = new StringBuilder();
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();
		String[] reactions = new String[0];
		final String[] slashArgs;
		try
		{
			slashArgs = args[0].split("/");
		}
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("A poll needs at least 2 options!").color(Color.RED).sendTemporary();
			return;
		}

		if(slashArgs.length >= 3)
		{
			String topic = slashArgs[0];
			for(int i=1; i < slashArgs.length && i < Messaging.REACTION_LIMIT+1; i++)
			{
				options.append(slashArgs[i]).append(" ").append(Command_Utils.POLL_EMOJIS[i - 1]).append("\n");
				if(args.length <= Messaging.REACTION_LIMIT/2+1) options.append("\n");
				reactions = Common.append(reactions, Command_Utils.POLL_EMOJIS_UNICODE[i-1]);
			}
			new EmbedGenerator(channel)
					.title("Poll:")
					.text(topic)
					.element("Options:", options.toString())
					.footer("Poll created by "+ author.getAsTag())
					.thumbnail(author.getAvatarUrl())
					.color(Color.YELLOW)
					.reaction(reactions)
					.send();
		}
		else
		{
			new EmbedGenerator(channel).text("A poll needs at least 2 options!").color(Color.RED).sendTemporary();
		}
	}
}
