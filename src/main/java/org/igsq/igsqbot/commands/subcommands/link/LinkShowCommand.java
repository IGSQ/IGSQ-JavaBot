package org.igsq.igsqbot.commands.subcommands.link;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
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
	public void run(List<String> args, CommandContext ctx)
	{
		Minecraft minecraft = ctx.getIGSQBot().getMinecraft();

		if(!ctx.isFromGuild())
		{
			showSelf(MinecraftUtils.getLinks(ctx.getAuthor().getId(), minecraft), ctx);
			return;
		}

		if(args.isEmpty())
		{
			showSelf(MinecraftUtils.getLinks(ctx.getAuthor().getId(), minecraft), ctx);
			return;
		}

		if(ctx.memberPermissionCheck(Permission.MESSAGE_MANAGE))
		{
			String arg = args.get(0);
			new Parser(arg, ctx).parseAsUser(user ->
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

				ctx.getChannel().sendMessage(new EmbedBuilder()
						.setTitle("Links for user: " + user.getAsTag())
						.setDescription(text.length() == 0 ? "No links found" : text.toString())
						.setColor(Constants.IGSQ_PURPLE)
						.build()).queue();
			});
			return;
		}

		showSelf(MinecraftUtils.getLinks(ctx.getAuthor().getId(), minecraft), ctx);
	}

	private void showSelf(List<MinecraftUtils.Link> links, CommandContext ctx)
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
