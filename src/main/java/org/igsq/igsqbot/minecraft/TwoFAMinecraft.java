package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Constants;
import org.igsq.igsqbot.IGSQBot;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.EmbedUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TwoFAMinecraft
{
	private final IGSQBot igsqBot;

	public TwoFAMinecraft(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		start();
	}

	public void start()
	{
		igsqBot.getTaskHandler().addRepeatingTask(() ->
		{
			for(String selectedID : getPending())
			{
				sendDirectMessage(selectedID);
			}
		}, "2FATask", 0, TimeUnit.SECONDS, 2);
	}

	private void sendDirectMessage(String id)
	{
		String code = generateCode();
		igsqBot.getShardManager().retrieveUserById(id)
				.flatMap(User::openPrivateChannel)
				.queue(
						channel ->
						{
							channel.sendMessage(new EmbedBuilder()
									.setDescription("Here is your Minecraft 2FA Code: `" + code + "`\n If you did not request this code, please ignore this message.")
									.setColor(Constants.IGSQ_PURPLE)
									.build()).queue(
									message ->
									{
										igsqBot.getDatabase().updateCommand("UPDATE discord_2fa SET code = '" + code + "' WHERE uuid = '" + MinecraftUtils.getUUIDFromID(id, igsqBot) + "';");

										igsqBot.getTaskHandler().addTask(() ->
										{
											EmbedUtils.sendReplacedEmbed(message, new EmbedBuilder(message.getEmbeds().get(0)).setDescription("Here is your Minecraft 2FA Code: **EXPIRED**\n If you did not request this code, please ignore this message."), true);
											if(igsqBot.getDatabase().scalarCommand("SELECT COUNT(*) FROM discord_2fa WHERE uuid = '" + MinecraftUtils.getUUIDFromID(id, igsqBot) + "' AND current_status = 'pending';") > 0)
											{
												igsqBot.getDatabase().updateCommand("UPDATE discord_2fa SET current_status = 'expired' WHERE uuid = '" + MinecraftUtils.getUUIDFromID(id, igsqBot) + "';");
											}
											igsqBot.getDatabase().updateCommand("UPDATE discord_2fa SET code = NULL WHERE uuid = '" + MinecraftUtils.getUUIDFromID(id, igsqBot) + "';");
										}, TimeUnit.SECONDS, 60);
									}, error -> {}
							);
						}

				);
	}

	private String generateCode()
	{
		return String.format("%06d", new Random().nextInt(999999));
	}

	private List<String> getPending()
	{
		ResultSet discord_2fa = igsqBot.getDatabase().queryCommand("SELECT * FROM discord_2fa WHERE current_status = 'pending' AND `code` IS NULL");
		List<String> pendingIDs = new ArrayList<>();

		if(discord_2fa == null)
		{
			return pendingIDs;
		}
		try
		{
			while(discord_2fa.next())
			{
				pendingIDs.add(MinecraftUtils.getIDFromUUID(discord_2fa.getString(1), igsqBot));
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return pendingIDs;
	}
}
