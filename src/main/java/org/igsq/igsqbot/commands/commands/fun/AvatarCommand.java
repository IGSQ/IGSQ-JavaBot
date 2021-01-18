package org.igsq.igsqbot.commands.commands.fun;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.util.CommandChecks;

@SuppressWarnings("unused")
public class AvatarCommand extends Command
{
	public AvatarCommand()
	{
		super("Avatar", "Shows the avatar for the specified user(s).", "<users {3}>");
		addAliases("avatar", "avi", "pfp");
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		Message message = ctx.getMessage();
		MessageChannel channel = ctx.getChannel();
		User author = message.getAuthor();

		CommandChecks.argsSizeExceeds(ctx, 3);
		if(message.getMentionedMembers().isEmpty())
		{
			channel.sendMessage(new EmbedBuilder()
					.setTitle(author.getAsTag() + "'s Avatar")
					.setImage(author.getAvatarUrl() + "?size=4096")
					.setColor(Constants.IGSQ_PURPLE)
					.build()).queue();
		}
		else
		{
			message.getMentionedMembers().forEach(member ->
					channel.sendMessage(new EmbedBuilder()
							.setTitle(member.getUser().getAsTag() + "'s Avatar")
							.setImage(member.getUser().getEffectiveAvatarUrl() + "?size=4096")
							.setColor(Constants.IGSQ_PURPLE).build()).queue());
		}
	}
}
