package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.entities.Command;
import org.igsq.igsqbot.entities.CommandContext;
import org.igsq.igsqbot.entities.EmbedGenerator;
import org.igsq.igsqbot.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarnCommand extends Command
{
	public WarnCommand()
	{
		super("Warn", new String[]{"warn"}, "Handles the user warning system", "[user][reason] | [show][user] | [remove][user][number]", new Permission[]{}, true, 0);
	}

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
					try
					{
						removeWarning(member, channel, Integer.parseInt(args.get(2)));
					}
					catch(Exception exception)
					{
						EmbedUtils.sendSyntaxError(channel, this);
						new ErrorHandler(exception);
					}
					break;
				default:
					EmbedUtils.sendSyntaxError(channel, this);
			}
		}
	}

	private void addWarning(Member member, MessageChannel channel, String reason)
	{
		Yaml.updateField(
				member.getGuild().getId() + "." + member.getId() + ".warnings",
				Filename.PUNISHMENT,

				YamlUtils.getFieldAppended(
						member.getGuild().getId() + "." + member.getId() + ".warnings",
						Filename.PUNISHMENT,
						"\n",
						reason + " - " + StringUtils.getTimestamp()));
		EmbedUtils.sendSuccess(channel, "Warned " + member.getAsMention() + " for reason: " + reason);
	}

	private void showWarning(Member member, MessageChannel channel)
	{
		EmbedGenerator embed = new EmbedGenerator(channel);
		StringBuilder embedText = new StringBuilder();
		int currentWarning = 1;
		for(String selectedWarning : getWarnings(member))
		{
			embedText.append(currentWarning).append(": ").append(selectedWarning).append("\n");
			currentWarning ++;
		}

		embed
			.title("Warnings for " + member.getUser().getAsTag())
			.text(embedText.length() == 0 ? "This user has no warnings" : embedText.toString())
			.color(Constants.IGSQ_PURPLE)
			.send();
	}

	private void removeWarning(Member member, MessageChannel channel, int number)
	{
		List<String> warnings = getWarnings(member);
		number--;
		if(number < 0 || number > warnings.size())
		{
			EmbedUtils.sendSyntaxError(channel, this);
			return;
		}
		if(warnings.get(number) != null)
		{
			String removedWarning = warnings.remove(number);
			Yaml.updateField(member.getGuild().getId() + "." + member.getId() + ".warnings",
					Filename.PUNISHMENT,
					ArrayUtils.arrayCompile(warnings, "\n"));
			EmbedUtils.sendSuccess(channel, "Removed warning: " + removedWarning + " from user " + member.getAsMention());
		}
	}

	private List<String> getWarnings(Member member)
	{
		if(YamlUtils.isFieldEmpty(member.getGuild().getId() + "." + member.getId() + ".warnings", Filename.PUNISHMENT))
		{
			return new ArrayList<>();
		}
		else
		{
			return new ArrayList<>(Arrays.asList(Yaml.getFieldString(member.getGuild().getId() + "." + member.getId() + ".warnings", Filename.PUNISHMENT).split("\n")));
		}
	}
}
