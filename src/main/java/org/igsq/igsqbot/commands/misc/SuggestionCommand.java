package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class SuggestionCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		User author = ctx.getAuthor();

		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
			return;
		}

		if(CommandUtils.isArgsEmbedCompatible(args))
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			ctx.getChannel().sendMessage(new EmbedBuilder()
					.setTitle("Suggestion:")
					.setDescription(ArrayUtils.arrayCompile(args, " "))
					.setColor(Constants.IGSQ_PURPLE)
					.setThumbnail(author.getAvatarUrl())
					.setFooter("Suggestion by: " + ctx.getMember().getEffectiveName() + author.getDiscriminator())
					.setTimestamp(Instant.now())
					.build()).queue();
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