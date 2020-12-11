package org.igsq.igsqbot.commands;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.util.Embed_Utils;

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

		if(message.getMentionedUsers().size() <= 3 && message.getMentionedUsers().size() > 1)
		{
			for(User selectedUser : message.getMentionedUsers())
			{
				new EmbedGenerator(channel)
						.title(selectedUser.getAsTag() + "'s Avatar")
						.image(selectedUser.getEffectiveAvatarUrl())
						.color(Common.IGSQ_PURPLE).send();
			}
		}
		else if(message.getMentionedUsers().size() > 3)
		{
			Embed_Utils.sendError(channel, "Too many users entered! (Limit: 3)");
		}
		else
		{
			new EmbedGenerator(channel)
					.title(author.getAsTag() + "'s Avatar")
					.image(author.getAvatarUrl())
					.color(Common.IGSQ_PURPLE)
					.send();
		}
	}
}

