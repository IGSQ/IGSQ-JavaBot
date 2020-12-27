package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.entities.yaml.Punishment;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.util.Collections;
import java.util.List;

public class WarnCommand extends Command
{
	@Override
	public void execute(List<String> args, CommandContext ctx)
	{
		final MessageChannel channel = ctx.getChannel();
		final Guild guild = ctx.getGuild();
		if(args.size() <= 1)
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
		else if(UserUtils.isUserMention(args.get(0)))
		{
			if(UserUtils.getMemberFromUser(ctx.getAuthor(), guild).hasPermission(Permission.MESSAGE_MANAGE))
			{
				EmbedUtils.sendPermissionError(channel, this);
			}
			else
			{
				Member member = UserUtils.getMemberFromUser(UserUtils.getUserFromMention(args.get(0)), guild);
				if(!UserUtils.getMemberFromUser(ctx.getAuthor(), guild).canInteract(member) || member.isOwner() || member.getUser().isBot())
				{
					EmbedUtils.sendError(channel, "You cannot warn this user!");
				}
				else
				{
					args.remove(0);
					addWarning(member, channel, ArrayUtils.arrayCompile(args, " "));
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

			final Member member = UserUtils.getMemberFromUser(UserUtils.getUserFromMention(args.get(1)), guild);
			switch(args.get(0).toLowerCase())
			{
				case "show":
					showWarning(member, channel);
					break;
				case "remove":
					if(UserUtils.getMemberFromUser(ctx.getAuthor(), guild).hasPermission(Permission.MESSAGE_MANAGE))
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
	public boolean isRequiresGuild()
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
		Punishment punishment = new Punishment(member);
		punishment.addWarning(reason);
		EmbedUtils.sendSuccess(channel, "Warned " + member.getAsMention() + " for reason: " + reason);
	}

	private void showWarning(Member member, MessageChannel channel)
	{
		EmbedGenerator embed = new EmbedGenerator(channel);
		Punishment punishment = new Punishment(member);
		String embedText = punishment.compileWarnings();

		embed
			.title("Warnings for " + member.getUser().getAsTag())
			.text(embedText.length() == 0 ? "This user has no warnings" : embedText)
			.color(Constants.IGSQ_PURPLE)
			.send();
	}

	private void removeWarning(Member member, MessageChannel channel, int number)
	{
		Punishment punishment = new Punishment(member);
		String removedWarning = punishment.removeWarning(--number);
		if(removedWarning != null)
		{
			EmbedUtils.sendSuccess(channel, "Removed warning: " + removedWarning + " from user " + member.getAsMention());
		}
		else
		{
			EmbedUtils.sendSyntaxError(channel, this);
		}
	}
}
