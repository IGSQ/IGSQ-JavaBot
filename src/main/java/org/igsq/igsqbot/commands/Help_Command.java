package main.java.org.igsq.igsqbot.commands;

import main.java.org.igsq.igsqbot.Yaml;
import main.java.org.igsq.igsqbot.objects.Command;
import main.java.org.igsq.igsqbot.objects.Context;
import main.java.org.igsq.igsqbot.objects.EmbedGenerator;
import main.java.org.igsq.igsqbot.util.ArrayUtils;
import main.java.org.igsq.igsqbot.util.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class Help_Command extends Command
{
	public Help_Command()
	{
		super("help", new String[]{"?", "howto", "commands"}, "Shows the help menu for this bot", "[none]", new Permission[]{}, false, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final EmbedGenerator embed = ArrayUtils.HELP_PAGE_TEXT.get(0);
		embed.setChannel(channel).color(EmbedUtils.IGSQ_PURPLE);
		final User author = ctx.getAuthor();

		if(!args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else
		{
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
}
