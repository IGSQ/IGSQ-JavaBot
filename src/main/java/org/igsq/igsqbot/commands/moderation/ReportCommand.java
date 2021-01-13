package org.igsq.igsqbot.commands.moderation;

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

import java.util.Collections;
import java.util.List;

public class ReportCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		MessageChannel channel = ctx.getChannel();
		User author = ctx.getAuthor();
		Guild guild = ctx.getGuild();
		MessageChannel reportChannel = guild.getTextChannelById(new GuildConfig(guild, ctx).getReportChannel());
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
				CommandUtils.interactionCheck(author, user, ctx, () ->
				{
					UserUtils.getMemberFromUser(user, guild).queue(
							member ->
							{
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
				});
			}
		});
	}

	@Override
	public String getName()
	{
		return "Report";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("report");
	}

	@Override
	public String getDescription()
	{
		return "Reports the specified member with the specified reason";
	}

	@Override
	public String getSyntax()
	{
		return "[user] [reason]";
	}

	@Override
	public boolean canExecute(CommandContext ctx)
	{
		return true;
	}

	@Override
	public boolean isGuildOnly()
	{
		return true;
	}

	@Override
	public long getCooldown()
	{
		return 60;
	}
}
