package org.igsq.igsqbot.improvedcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.util.Command_Utils;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.Yaml;

public class Help_Command extends Command
{
	public Help_Command()
	{
		super("help", new String[]{"?", "howto", "commands"}, "Shows the help menu for this bot", new Permission[]{}, false, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final EmbedGenerator embed = Command_Utils.HELP_PAGE_TEXT[0];
		embed.setChannel(channel);
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
