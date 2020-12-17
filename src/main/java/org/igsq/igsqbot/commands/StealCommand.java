package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.net.URL;
import java.util.List;

public class StealCommand extends Command
{
	public StealCommand()
	{
		super("Steal", new String[]{"steal"}, "Steals the specified image and adds it as an emoji", "[name][imageURL(A-Z + _)]", new Permission[]{Permission.MANAGE_EMOTES}, true, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		if(args.size() != 2 || !StringUtils.isURLValid(args.get(1)) || !args.get(0).matches("([A-Z]|[a-z]|_)\\w+"))
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(!UserUtils.basicPermCheck(ctx.getGuild().getSelfMember(), (TextChannel) channel))
		{
			EmbedUtils.sendPermissionError(channel, this);
		}
		else
		{
			Icon icon;
			try
			{
				icon = Icon.from(new URL(args.get(1)).openStream());

			}
			catch(Exception exception)
			{
				new ErrorHandler(exception);
				return;
			}
			ctx.getGuild().createEmote(args.get(0), icon).queue(
					emote ->
					{
						EmbedUtils.sendSuccess(channel, "Added emote " + emote.getAsMention() + " successfully!");
					},
					error ->
					{
						EmbedUtils.sendError(channel, "An error occurred while adding the emote.");
						new ErrorHandler(new Exception(error));
					});
		}
	}
}
