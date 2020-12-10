package org.igsq.igsqbot.improvedcommands;

import java.awt.Color;

import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Avatar_Command extends Command
{
	public Avatar_Command()
	{
		super("avatar", new String[]{}, "Shows the avatar for the mentioned user(s)", new Permission[]{}, false, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final Message message = ctx.getMessage();
		final MessageChannel channel = ctx.getChannel();
		final User author = message.getAuthor();

		if(message.getMentionedUsers().size() <= 3)
		{
			for(User selectedUser : message.getMentionedUsers())
			{
				new EmbedGenerator(channel)
						.title(selectedUser.getName() + "'s Avatar")
						.image(selectedUser.getEffectiveAvatarUrl())
						.color(Color.ORANGE).send();
			}
		}
		else if(message.getMentionedUsers().size() > 3)
		{
			new EmbedGenerator(channel).text("Too many users entered! (Limit: 3)").color(Color.RED).sendTemporary();
		}
		else
		{
			new EmbedGenerator(channel)
					.title(author.getName() + "'s Avatar")
					.image(author.getAvatarUrl())
					.send();
		}
	}
}

