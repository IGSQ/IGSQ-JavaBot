package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;

public class Modhelp_Command extends Command
{
	public Modhelp_Command()
	{
		super("modhelp", new String[]{}, "Shows the modhelp menu for this bot", "[none]",new Permission[]{Permission.MESSAGE_MANAGE}, false, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final EmbedGenerator embed = ArrayUtils.MODPAGE_TEXT.get(0);
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