package org.igsq.igsqbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.objects.GUIGenerator;
import org.igsq.igsqbot.util.*;

import java.awt.*;

public class Warn_Command extends Command
{
	public Warn_Command()
	{
		super("warn", new String[]{}, "Handles the user warning system", new Permission[]{}, true, 0);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final User warnTarget;
		final MessageChannel channel = ctx.getChannel();
		final String action;
		final Guild guild = ctx.getGuild();
		final User author = ctx.getAuthor();

		if(args.length > 1 && User_Utils.isUserMention(args[0]))
		{
			warnTarget = User_Utils.getUserFromMention(args[0]);
			if(warnTarget == null)
			{
				Embed_Utils.sendError(channel, "Enter an valid user.");
			}
			else
			{
				addWarning(warnTarget, String.join("", Array_Utils.depend(args, 0)), guild.getId(), channel);
			}
		}
		else if(args.length > 1 && !User_Utils.isUserMention(args[0]))
		{
			action = args[0];

			warnTarget = User_Utils.getUserFromMention(args[1]);
			if(warnTarget == null)
			{
				Embed_Utils.sendError(channel, "Enter an valid user.");
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
				Embed_Utils.sendError(channel, "Enter a valid action.");
			}
		}
		else
		{
			Embed_Utils.sendError(channel, "Invalid syntax.");
		}
	}

	private void addWarning(User user, String reason, String guildId, MessageChannel channel)
	{
		Yaml_Utils.fieldAppend(guildId + ".warnings." + user.getId(), "punishment",reason + " - " + String_Utils.getTimestamp(), ",");

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
		embed.text(embedText.length() > 0 ? embedText.toString() : "This user has no warnings.").footer(String_Utils.getTimestamp()).color(Common.IGSQ_PURPLE).send();
	}

	private void removeWarning(User user, String guildId, MessageChannel channel, User author)
	{
		String[] warnings = Yaml.getFieldString(guildId + ".warnings." + user.getId(), "punishment").split(",");

		if(warnings.length == 0)
		{
			Embed_Utils.sendError(channel, "That user has no warnings.");
			return;
		}
		StringBuilder embedText = new StringBuilder();

		for(int i = 1; i < warnings.length; i++)
		{
			embedText.append(i).append(": ").append(warnings[i]);
		}
		EmbedGenerator embed = new EmbedGenerator(channel).title("Select a warning to remove.").color(Common.IGSQ_PURPLE).text(embedText.toString());
		GUIGenerator gui = new GUIGenerator(embed);
		int chosenWarning = gui.menu(author, 60000, warnings.length);

		if(chosenWarning != -1)
		{
			warnings = Array_Utils.depend(warnings, chosenWarning-1);
			Yaml.updateField(guildId + ".warnings." + user.getId(), "punishment", Array_Utils.arrayCompile(warnings, ","));

			new EmbedGenerator(channel).text("Removed warning: " + warnings[chosenWarning-1]).color(Color.GREEN).sendTemporary();
		}
	}
}
