package org.igsq.igsqbot.commands.misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.info.BotInfo;
import org.igsq.igsqbot.entities.info.GuildInfo;
import org.igsq.igsqbot.entities.info.MemberInfo;
import org.igsq.igsqbot.entities.info.RoleInfo;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Collections;
import java.util.List;

public class InfoCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		if(args.isEmpty())
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			switch(args.get(0))
			{
				case "user" -> userInfo(ctx, args);
				case "role" -> roleInfo(ctx, args);
				case "bot" -> botInfo(ctx);
				case "server" -> serverInfo(ctx, args);
				default -> EmbedUtils.sendSyntaxError(ctx);
			}
		}
	}

	private void userInfo(CommandContext ctx, List<String> args)
	{
		if(args.size() < 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			Guild guild = ctx.getGuild();
			new Parser(ArrayUtils.arrayCompile(args.subList(1, args.size()), " "), ctx).parseAsUser(user ->
					UserUtils.getMemberFromUser(user, guild).queue(member ->
					{
						MemberInfo memberInfo = new MemberInfo(member);


						ctx.getChannel().sendMessage(new EmbedBuilder()
								.setTitle("Info for " + memberInfo.getNickname())
								.setDescription("")
								.build()).queue();
					},
					error -> ctx.replyError("Member not found.")));
		}
	}

	private void roleInfo(CommandContext ctx, List<String> args)
	{
		if(args.size() < 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			new Parser(ArrayUtils.arrayCompile(args.subList(1, args.size()), " "), ctx).parseAsRole(role ->
			{
				RoleInfo roleInfo = new RoleInfo(role);

				roleInfo.getMembers().onSuccess(members ->
				{
					int size = members.size();
					StringBuilder text = new StringBuilder();

					if(size > 5)
					{
						members = members.subList(0, 5);
					}

					text.append(roleInfo.getAsMention())
							.append(" | ")
							.append(size)
							.append(" members")
							.append("\n")
							.append("**Members:**")
							.append("\n");

					members.forEach(member -> text.append(member.getAsMention()).append(" "));

					ctx.getChannel().sendMessage(new EmbedBuilder()
							.setDescription(text.toString())
							.setColor(Constants.IGSQ_PURPLE)
							.build()).queue();
				});
			});
		}
	}

	private void botInfo(CommandContext ctx)
	{
		BotInfo botInfo = new BotInfo(ctx.getIGSQBot());

		ctx.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("IGSQBot information")
				.addField("JVM Version", botInfo.getJavaVersion(), true)
				.addField("Java Vendor", botInfo.getJavaVendor(), true)
				.addBlankField(true)
				.addField("Thread Count", String.valueOf(botInfo.getThreadCount()), true)
				.addField("Memory Usage", botInfo.getMemoryFormatted(), true)
				.addBlankField(true)

				.addField("Shard count", String.valueOf(botInfo.getTotalShards()), true)
				.addField("Server count", String.valueOf(botInfo.getTotalServers()), true)

				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue();
	}

	private void serverInfo(CommandContext ctx, List<String> args)
	{
		if(args.size() < 2)
		{
			EmbedUtils.sendSyntaxError(ctx);
		}
		else
		{
			Guild guild = new Parser(args.get(1), ctx).parseAsGuild();

			if(guild != null)
			{
				GuildInfo guildInfo = new GuildInfo(guild);
			}
		}
	}


	@Override
	public String getName()
	{
		return "Info";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("info");
	}

	@Override
	public String getDescription()
	{
		return "Provides information for things.";
	}

	@Override
	public String getSyntax()
	{
		return "";
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
	public int getCooldown()
	{
		return 0;
	}
}
