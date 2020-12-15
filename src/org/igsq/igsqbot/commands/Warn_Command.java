package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.GUIGenerator;
import org.igsq.igsqbot.util.*;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class Warn_Command extends Command
{
	public Warn_Command()
	{
		super("warn", new String[]{}, "Handles the user warning system","[user][reason] | [show|remove][user]", new Permission[]{}, true, 0);
	}

	@Override
	public void execute(List<String> args, Context ctx)
	{
		final User warnTarget;
		final MessageChannel channel = ctx.getChannel();
		final String action;
		final Guild guild = ctx.getGuild();
		final User author = ctx.getAuthor();


		if(args.size() == 1 && UserUtils.isUserMention(args.get(0)))
		{
			args.remove(0);
			warnTarget = UserUtils.getUserFromMention(args.get(0));
			if(warnTarget == null)
			{
				EmbedUtils.sendSyntaxError(channel,this);
			}
			else
			{
				addWarning(warnTarget, ArrayUtils.arrayCompile(args, " "), guild.getId(), channel);
			}
		}
		else if(args.size() > 1 && !UserUtils.isUserMention(args.get(0)))
		{
			action = args.get(0);
			warnTarget = UserUtils.getUserFromMention(args.get(1));
			if(warnTarget == null)
			{
				EmbedUtils.sendSyntaxError(channel,this);
				return;
			}

			if(action.equalsIgnoreCase("show") || action.equalsIgnoreCase("list"))
			{
				showWarning(warnTarget, channel, guild.getId());
			}
			else if(action.equalsIgnoreCase("remove"))
			{
				removeWarning(warnTarget, guild.getId(), channel, author);
			}
			else
			{
				EmbedUtils.sendSyntaxError(channel,this);
			}
		}
		else
		{
			EmbedUtils.sendSyntaxError(channel,this);
		}
	}

	private void addWarning(User user, String reason, String guildId, MessageChannel channel)
	{
		YamlUtils.fieldAppend(guildId + ".warnings." + user.getId(), "punishment",reason + " - " + StringUtils.getTimestamp(), ",");

		new EmbedGenerator(channel).text("Warned " + user.getAsMention() + " for reason: " + reason).color(Color.GREEN).sendTemporary(2000);

	}

	private void showWarning(User user, MessageChannel channel, String guildId)
	{
		final EmbedGenerator embed = new EmbedGenerator(channel).title("Warnings for " + user.getAsTag());
		final StringBuilder embedText = new StringBuilder();
		for(String selectedWarning : Yaml.getFieldString(guildId + ".warnings." + user.getId(), "punishment").split(","))
		{
			embedText.append(selectedWarning).append("\n");
		}
		embed.text(embedText.length() > 0 ? embedText.toString() : "This user has no warnings.").footer(StringUtils.getTimestamp()).color(EmbedUtils.IGSQ_PURPLE).send();
	}

	private void removeWarning(User user, String guildId, MessageChannel channel, User author)
	{
		List<String> warnings = Arrays.asList(Yaml.getFieldString(guildId + ".warnings." + user.getId(), "punishment").split(","));

		if(warnings.size() == 0)
		{
			EmbedUtils.sendError(channel, "That user has no warnings.");
			return;
		}
		StringBuilder embedText = new StringBuilder();

		for(int i = 1; i < warnings.size(); i++)
		{
			embedText.append(i).append(": ").append(warnings.get(i));
		}
		EmbedGenerator embed = new EmbedGenerator(channel).title("Select a warning to remove.").color(EmbedUtils.IGSQ_PURPLE).text(embedText.toString());
		GUIGenerator gui = new GUIGenerator(embed);
		int chosenWarning = gui.menu(author, 60000, warnings.size());

		if(chosenWarning != -1)
		{
			warnings.remove(chosenWarning-1);
			Yaml.updateField(guildId + ".warnings." + user.getId(), "punishment", ArrayUtils.arrayCompile(warnings, ","));

			new EmbedGenerator(channel).text("Removed warning: " + warnings.get(chosenWarning-1)).color(Color.GREEN).sendTemporary();
		}
	}
}
