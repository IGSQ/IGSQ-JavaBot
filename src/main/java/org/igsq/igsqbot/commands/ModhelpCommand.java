package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.CommandContext;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.cache.MessageDataCache;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModhelpCommand extends Command
{
	public ModhelpCommand()
	{
		super("Modhelp", new String[]{"modhelp"}, "Shows the modhelp menu for this bot", "[none]", new Permission[]{Permission.MESSAGE_MANAGE}, false, 0);
	}

	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final EmbedGenerator embed = ArrayUtils.MODPAGE_TEXT.get(0);
		embed.setChannel(channel).color(EmbedUtils.IGSQ_PURPLE);
		final User author = ctx.getAuthor();
		final JDA jda = ctx.getJDA();
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
								final MessageDataCache messageDataCache = new MessageDataCache(message.getId(), jda);
								final Map<String, String> users = new ConcurrentHashMap<>();

								users.put("user", author.getId());
								messageDataCache.setType(MessageDataCache.MessageType.HELP);
								messageDataCache.setUsers(users);
								messageDataCache.setPage(1);
								messageDataCache.build();

								message.addReaction("U+25C0").queue();
								message.addReaction("U+25B6").queue();
							}
					);
		}
	}
}