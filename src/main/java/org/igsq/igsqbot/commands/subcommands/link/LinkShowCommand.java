package org.igsq.igsqbot.commands.subcommands.link;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.minecraft.Minecraft;
import org.igsq.igsqbot.minecraft.MinecraftUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.UserUtils;

public class LinkShowCommand extends Command
{
	public LinkShowCommand(Command parent)
	{
		super(parent, "show", "Shows Minecraft links.", "[none]");
	}

	@Override
	public void run(List<String> args, CommandEvent cmd, Consumer<CommandException> failure)
	{
		Minecraft minecraft = cmd.getIGSQBot().getMinecraft();

		if(!cmd.isFromGuild())
		{
			showSelf(MinecraftUtils.getLinks(cmd.getAuthor().getId(), minecraft), cmd);
			return;
		}

		if(args.isEmpty())
		{
			showSelf(MinecraftUtils.getLinks(cmd.getAuthor().getId(), minecraft), cmd);
			return;
		}

		if(cmd.memberPermissionCheck(Permission.MESSAGE_MANAGE))
		{
			String arg = args.get(0);
			new Parser(arg, cmd).parseAsUser(user ->
			{
				List<MinecraftUtils.Link> links = MinecraftUtils.getLinks(user.getId(), minecraft);
				StringBuilder text = new StringBuilder();

				for(MinecraftUtils.Link link : links)
				{
					text
							.append(UserUtils.getAsMention(link.getId()))
							.append(" -- ")
							.append(MinecraftUtils.getName(link.getUuid(), minecraft))
							.append(" -- ")
							.append(MinecraftUtils.prettyPrintLinkState(link.getLinkState()))
							.append("\n");
				}

				cmd.getChannel().sendMessage(new EmbedBuilder()
						.setTitle("Links for user: " + user.getAsTag())
						.setDescription(text.length() == 0 ? "No links found" : text.toString())
						.setColor(Constants.IGSQ_PURPLE)
						.build()).queue();
			});
			return;
		}

		showSelf(MinecraftUtils.getLinks(cmd.getAuthor().getId(), minecraft), cmd);
	}

	private void showSelf(List<MinecraftUtils.Link> links, CommandEvent ctx)
	{
		StringBuilder text = new StringBuilder();
		Minecraft minecraft = ctx.getIGSQBot().getMinecraft();
		for(MinecraftUtils.Link link : links)
		{
			text
					.append(UserUtils.getAsMention(link.getId()))
					.append(" -- ")
					.append(MinecraftUtils.getName(link.getUuid(), minecraft))
					.append(" -- ")
					.append(MinecraftUtils.prettyPrintLinkState(link.getLinkState()))
					.append("\n");
		}

		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("Links for user: " + ctx.getAuthor().getAsTag())
				.setDescription(text.length() == 0 ? "No links found" : text.toString())
				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue();
	}
}
