package org.igsq.igsqbot.commands.commands.moderation;

import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.database.GuildConfig;
import org.igsq.igsqbot.entities.database.Report;
import org.igsq.igsqbot.util.*;

public class ReportCommand extends Command
{
	public ReportCommand()
	{
		super("Report", "Reports the specified member with the specified reason", "[user] [reason]");
		addAliases("report");
		guildOnly();
		autoDelete();
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();
		User author = ctx.getAuthor();
		Guild guild = ctx.getGuild();
		MessageChannel reportChannel = guild.getTextChannelById(new GuildConfig(ctx).getReportChannel());
		if(args.size() < 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
			return;
		}
		if(reportChannel == null)
		{
			ctx.replyError("There is no report channel setup");
			return;
		}

		new Parser(args.get(0), ctx).parseAsUser(user ->
		{
			args.remove(0);
			String reason = ArrayUtils.arrayCompile(args, " ");
			String messageLink = StringUtils.getMessageLink(ctx.getMessage().getIdLong(), channel.getIdLong(), guild.getIdLong());
			if(user.isBot())
			{
				ctx.replyError("You may not report bots.");
			}
			else
			{
				UserUtils.getMemberFromUser(user, guild).queue(
						member ->
						{
							if(member.isOwner())
							{
								ctx.replyError("You may not report the owner.");
								return;
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
												new Report(message, ctx.getMessage(), channel, guild, user, reason, ctx.getIGSQBot()).add();
												message.addReaction(Constants.THUMB_UP).queue();
											}
									);
						}
				);
			}
		});
	}
}
