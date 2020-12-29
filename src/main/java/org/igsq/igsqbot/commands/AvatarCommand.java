package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
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
					channel.sendMessage(new EmbedBuilder()
							.setTitle(member.getUser().getAsTag() + "'s Avatar")
							.setImage(member.getUser().getEffectiveAvatarUrl() + "?size=4096")
							.setColor(Constants.IGSQ_PURPLE).build()).queue());

		}
		else
		{
			channel.sendMessage(new EmbedBuilder()
					.setTitle(author.getAsTag() + "'s Avatar")
					.setImage(author.getAvatarUrl() + "?size=4096")
					.setColor(Constants.IGSQ_PURPLE)
					.build()).queue();
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
	public boolean isGuildOnly()
	{
		return false;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}

