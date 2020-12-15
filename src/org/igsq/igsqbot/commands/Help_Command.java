package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.EmbedUtils;

public class Help_Command extends Command
{
	public Help_Command()
	{
		super("help", new String[]{"?", "howto", "commands"}, "Shows the help menu for this bot", "[none]", new Permission[]{}, false, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final EmbedGenerator embed = ArrayUtils.HELP_PAGE_TEXT.get(0);
		embed.setChannel(channel).color(EmbedUtils.IGSQ_PURPLE);
		final User author = ctx.getAuthor();

		channel.sendMessage(embed.getBuilder().build()).queue
				(
						message ->
						{
							Yaml.updateField(message.getId() + ".modhelp.user", "internal", author.getId());
							Yaml.updateField(message.getId() + ".modhelp.enabled", "internal", true);
							Yaml.updateField(message.getId() + ".modhelp.page", "internal", 1);
							message.addReaction("U+25C0").queue();
							message.addReaction("U+25B6").queue();
						}
				);
	}
}
