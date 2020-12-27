package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SuggestionCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final Guild guild = ctx.getGuild();
		final User author = ctx.getAuthor();
		final MessageChannel channel = ctx.getChannel();

		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}
		final TextChannel suggestionChannel = guild.getTextChannelById(Yaml.getFieldString(guild.getId() + ".suggestionchannel", Filename.GUILD));
		if(suggestionChannel != null)
		{
			if(!UserUtils.basicPermCheck(guild.getSelfMember(), suggestionChannel))
			{
				EmbedUtils.sendPermissionError(channel, this);
			}
			else if(CommandUtils.isArgsEmbedCompatible(args))
			{
				EmbedUtils.sendSyntaxError(channel, this);
			}
			else
			{
				new EmbedGenerator((MessageChannel) suggestionChannel)
						.title("Suggestion:")
						.text(ArrayUtils.arrayCompile(args, " "))
						.color(Constants.IGSQ_PURPLE)
						.thumbnail(author.getAvatarUrl())
						.footer("Suggestion by: " + UserUtils.getMemberFromUser(author, guild).getEffectiveName() + author.getDiscriminator())
						.send();
			}
		}
		else
		{
			EmbedUtils.sendError(channel, "There is no setup suggestion Channel");
		}
	}

	@Override
	public String getName()
	{
		return "Suggestion";
	}

	@Override
	public List<String> getAliases()
	{
		return Arrays.asList("suggest", "suggestion", "idea");
	}

	@Override
	public String getDescription()
	{
		return "Suggests an idea in the designated suggestion channel.";
	}

	@Override
	public String getSyntax()
	{
		return "[topic]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isRequiresGuild()
	{
		return true;
	}

	@Override
	public int getCooldown()
	{
		return 0;
	}
}