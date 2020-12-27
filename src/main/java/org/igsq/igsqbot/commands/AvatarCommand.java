package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.Arrays;
import java.util.List;

public class AvatarCommand extends Command
{

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final Message message = ctx.getMessage();
		final MessageChannel channel = ctx.getChannel();
		final User author = message.getAuthor();

		if(message.getMentionedMembers().size() > 3)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(!message.getMentionedMembers().isEmpty())
		{
			message.getMentionedMembers().forEach(member ->
					new EmbedGenerator(channel)
							.title(member.getUser().getAsTag() + "'s Avatar")
							.image(member.getUser().getEffectiveAvatarUrl() + "?size=4096")
							.color(Constants.IGSQ_PURPLE).send());

		}
		else
		{
			new EmbedGenerator(channel)
					.title(author.getAsTag() + "'s Avatar")
					.image(author.getAvatarUrl() + "?size=4096")
					.color(Constants.IGSQ_PURPLE)
					.send();
		}
	}

	@Override
	public String getName()
	{
		return "Avatar";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("avatar", "avi", "pfp");
	}

	@Override
	public String getDescription()
	{
		return "Shows the avatar for the specified user(s).";
	}

	@Override
	public String getSyntax()
	{
		return "<users {3}>";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isRequiresGuild()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}

