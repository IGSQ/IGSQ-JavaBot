package org.igsq.igsqbot.improvedcommands;

import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.dv8tion.jda.api.entities.*;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.commands.Main_Command;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.objects.Command;
import org.igsq.igsqbot.objects.Context;
import org.igsq.igsqbot.objects.EmbedGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Clear_Command extends Command
{
	public Clear_Command()
	{
		super("clear", new String[]{"purge"}, "Clears the channel with the specified amount", new Permission[]{Permission.MESSAGE_MANAGE}, 5);
	}

	@Override
	public void execute(String[] args, Context ctx)
	{
		final int amount;
		final MessageChannel channel = ctx.getChannel();
		final User author = ctx.getAuthor();

		try
		{
			amount = Integer.parseInt(args[0]);
		}
		catch(Exception exception)
		{
			new EmbedGenerator(channel).text("Enter a number as the amount.").color(Color.RED).sendTemporary();
			return;
		}
		if(CooldownHandler.isOnCooldown(author.getIdLong(), this))
		{
			new EmbedGenerator(channel).text("This command is on cooldown. (Remaining: " + CooldownHandler.getCooldown(author.getIdLong(), this) + "s").color(Color.RED).sendTemporary();
		}
		else if(amount <= 0)
		{
			new EmbedGenerator(channel).text("Invalid amount entered.").color(Color.RED).sendTemporary();
		}
		else if(amount > 51)
		{
			new EmbedGenerator(channel).text("You tried to delete too many messages (Limit: 50)").color(Color.RED).sendTemporary();
		}
		else
		{
			CooldownHandler.addCooldown(author.getIdLong(), this);
			channel.getHistory().retrievePast(amount).queue(
					messages ->
					{
						channel.purgeMessages(messages);
						new EmbedGenerator(channel).text("Deleted " + (messages.size()) + " messages").color(Color.GREEN).sendTemporary(5000);
					}
			);
		}
	}
}
