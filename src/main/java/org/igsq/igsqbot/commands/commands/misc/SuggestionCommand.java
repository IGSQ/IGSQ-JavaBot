package org.igsq.igsqbot.commands.commands.misc;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;

@SuppressWarnings("unused")
public class SuggestionCommand extends Command
{
	public SuggestionCommand()
	{
		super("Suggestion", "Suggests an idea in the designated suggestion channel.", "[topic]");
		addFlags(CommandFlag.GUILD_ONLY);
		addAliases("suggest", "suggestion", "idea");
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsEmpty(cmd, failure)) return;
		if(CommandChecks.argsEmbedCompatible(cmd, failure)) return;

		User author = cmd.getAuthor();
		GuildConfig guildConfig = new GuildConfig(cmd);
		MessageChannel suggestionChannel = cmd.getGuild().getTextChannelById(guildConfig.getSuggestionChannel());

		if(CommandChecks.channelConfigured(suggestionChannel, "Suggestion channel", failure)) return;
		suggestionChannel.sendMessage(new EmbedBuilder()
				.setTitle("Suggestion:")
				.setDescription(ArrayUtils.arrayCompile(args, " "))
				.setColor(Constants.IGSQ_PURPLE)
				.setThumbnail(author.getAvatarUrl())
				.setFooter("Suggested by: " + cmd.getAuthor().getAsTag() + " | ")
				.setTimestamp(Instant.now())
				.build()).queue(
				message ->
				{
					message.addReaction(Emoji.THUMB_UP.getAsReaction()).queue();
					message.addReaction(Emoji.THUMB_DOWN.getAsReaction()).queue();
				});

	}
}
