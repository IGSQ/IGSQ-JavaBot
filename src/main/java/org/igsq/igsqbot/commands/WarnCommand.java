package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.json.JsonPunishment;
import org.igsq.igsqbot.entities.json.JsonPunishmentCache;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.StringUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Collections;
import java.util.List;

public class WarnCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Member member = ctx.getMember();
		if(args.size() <= 1 || ctx.getMessage().getMentionedMembers().isEmpty())
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(UserUtils.isUserMention(args.get(0)))
		{
			if(!member.hasPermission(Permission.MESSAGE_MANAGE))
			{
				EmbedUtils.sendPermissionError(channel, this);
			}
			else
			{
				final Member targetMember = ctx.getMessage().getMentionedMembers().get(0);
				if(!member.canInteract(targetMember) || targetMember.isOwner() || targetMember.getUser().isBot())
				{
					EmbedUtils.sendError(channel, "You cannot warn this user!");
				}
				else
				{
					args.remove(0);
					addWarning(targetMember, channel, ArrayUtils.arrayCompile(args, " "));
				}
			}
		}
		else
		{
			if(args.size() < 2)
			{
				EmbedUtils.sendSyntaxError(channel, this);
				return;
			}

			final Member targetMember = ctx.getMessage().getMentionedMembers().get(0);
			switch(args.get(0).toLowerCase())
			{
				case "show":
					showWarning(targetMember, channel);
					break;
				case "remove":
					if(!member.hasPermission(Permission.MESSAGE_MANAGE))
					{
						EmbedUtils.sendPermissionError(channel, this);
					}
					else
					{
						try
						{
							removeWarning(member, channel, Integer.parseInt(args.get(2)));
						}
						catch(Exception exception)
						{
							new ErrorHandler(exception);
							EmbedUtils.sendSyntaxError(channel, this);
						}
					}
					break;
				default:
					EmbedUtils.sendSyntaxError(channel, this);
			}
		}
	}

	@Override
	public String getName()
	{
		return "Warn";
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("warn");
	}

	@Override
	public String getDescription()
	{
		return "Handles the user warning system";
	}

	@Override
	public String getSyntax()
	{
		return "[user][reason] | [show][user] | [remove][user][number]";
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

	private void addWarning(Member member, MessageChannel channel, String reason)
	{
		JsonPunishment jsonPunishment = JsonPunishmentCache.getInstance().get(member);
		List<String> warnings = jsonPunishment.getWarnings();
		warnings.add(reason + " - " + StringUtils.getTimestamp());
		jsonPunishment.setWarnings(warnings);
		EmbedUtils.sendSuccess(channel, "Warned " + member.getAsMention() + " for reason: " + reason);
	}

	private void showWarning(Member member, MessageChannel channel)
	{
		JsonPunishment jsonPunishment = JsonPunishmentCache.getInstance().get(member);
		List<String> warnings = jsonPunishment.getWarnings();
		StringBuilder embedText = new StringBuilder();
		int currentWarning = 1;

		for(String selectedWarning : warnings)
		{
			embedText.append(currentWarning).append(": ").append(selectedWarning).append("\n");
			currentWarning++;
		}
		channel.sendMessage(new EmbedBuilder()
				.setTitle("Warnings for " + member.getUser().getAsTag())
				.setDescription(embedText.length() == 0 ? "This user has no warnings" : embedText.toString())
				.setColor(Constants.IGSQ_PURPLE)
				.build()).queue();
	}

	private void removeWarning(Member member, MessageChannel channel, int number)
	{
		JsonPunishment jsonPunishment = JsonPunishmentCache.getInstance().get(member);
		List<String> warnings = jsonPunishment.getWarnings();

		int decrementedNumber = --number;
		if(decrementedNumber > warnings.size() || decrementedNumber < 0)
		{
			EmbedUtils.sendError(channel, "That warning does not exist.");
			return;
		}
		String removedWarning = warnings.get(decrementedNumber);
		warnings.remove(decrementedNumber);
		jsonPunishment.setWarnings(warnings);
		EmbedUtils.sendSuccess(channel, "Removed warning: " + removedWarning + " from user " + member.getAsMention());
	}
}
