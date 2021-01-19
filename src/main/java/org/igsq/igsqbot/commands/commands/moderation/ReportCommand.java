package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.igsq.igsqbot.entities.Emoji;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Report;
import org.igsq.igsqbot.util.*;

@SuppressWarnings("unused")
public class ReportCommand extends Command
{
	public ReportCommand()
	{
		super("Report", "Reports the specified member with the specified reason", "[user] [reason]");
		addAliases("report");
		addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsSizeSubceeds(ctx, 2);

		MessageChannel channel = ctx.getChannel();
		User author = ctx.getAuthor();
		Guild guild = ctx.getGuild();
		MessageChannel reportChannel = guild.getTextChannelById(new GuildConfig(ctx).getReportChannel());

		CommandChecks.channelConfigured(reportChannel, "Report channel");

		new Parser(args.get(0), ctx).parseAsUser(user ->
		{
			args.remove(0);
			String reason = ArrayUtils.arrayCompile(args, " ");
			String messageLink = StringUtils.getMessageLink(ctx.getMessage().getIdLong(), channel.getIdLong(), guild.getIdLong());

			if(user.isBot())
			{
				throw new IllegalArgumentException("You may not report bots.");
			}

			UserUtils.getMemberFromUser(user, guild).queue(
					member ->
					{
						if(member.isOwner())
						{
							throw new HierarchyException("You may not report the owner.");
						}
						reportChannel.sendMessage(new EmbedBuilder()
								.setTitle("New report by: " + author.getAsTag())
								.addField("Reporting user:", user.getAsMention(), false)
								.addField("Description:", reason, false)
								.addField("Channel:", StringUtils.getChannelAsMention(channel.getId()), false)
								.addField("Message Link:", "[Jump to message](" + messageLink + ")", false)
								.setColor(member.getColor())
								.setFooter("This report is unhandled and can only be dealt by members higher than " + member.getRoles().get(0).getName())
								.build()).queue
								(
										message ->
										{
											Report.add(message.getIdLong(), ctx.getMessage().getIdLong(), channel.getIdLong(), guild.getIdLong(), user.getIdLong(), reason, ctx.getIGSQBot());
											message.addReaction(Emoji.THUMB_UP.getUnicode()).queue();
										}
								);
					}
			);

		});
	}
}
