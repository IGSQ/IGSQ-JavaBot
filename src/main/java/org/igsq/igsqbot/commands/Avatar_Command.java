package main.java.org.igsq.igsqbot.commands;

import main.java.org.igsq.igsqbot.objects.Command;
import main.java.org.igsq.igsqbot.objects.Context;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class Avatar_Command extends Command
{
	public Avatar_Command()
	{
		super("avatar", new String[]{}, "Shows the avatar for the mentioned user(s)", "[user(s) -3-]" , new Permission[]{}, false, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		final Message message = ctx.getMessage();
		final MessageChannel channel = ctx.getChannel();
		final User author = message.getAuthor();

		if(message.getMentionedUsers().size() > 3)
		{
			EmbedUtils.sendSyntaxError(channel,this);
		}
		else if(message.getMentionedUsers().size() > 1)
		{
			message.getMentionedUsers().forEach(user -> new EmbedGenerator(channel)
					.title(user.getAsTag() + "'s Avatar")
					.image(user.getEffectiveAvatarUrl())
					.color(EmbedUtils.IGSQ_PURPLE).send());

		}
		else
		{
			new EmbedGenerator(channel)
					.title(author.getAsTag() + "'s Avatar")
					.image(author.getAvatarUrl())
					.color(EmbedUtils.IGSQ_PURPLE)
					.send();
		}
	}
}

