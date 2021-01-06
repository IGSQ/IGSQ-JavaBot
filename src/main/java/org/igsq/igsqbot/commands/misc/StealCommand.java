package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.FileUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Collections;
import java.util.List;

public class StealCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();

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
			Icon icon = FileUtils.getIcon(args.get(1));
			if(icon == null)
			{
				ctx.replyError("The image / gif provided could not be loaded.");
			}
			else
			{
				ctx.getGuild().createEmote(args.get(0), icon).queue(
						emote -> ctx.replySuccess("Added emote " + emote.getAsMention() + " successfully!"),
						error -> ctx.replyError("An error occurred while adding the emote."));
			}
		}
	}

	@Override
	public String getName()
	{
		return "Steal";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("steal");
	}

	@Override
	public String getDescription()
	{
		return "Steals the specified image and adds it as an emoji.";
	}

	@Override
	public String getSyntax()
	{
		return "[name{A-Z + _}] [imageURL]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return ctx.hasPermission(Collections.singletonList(Permission.MANAGE_EMOTES));
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}
