package org.igsq.igsqbot.minecraft;

import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.handlers.TaskHandler;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.util.EmbedUtils;
import org.igsq.igsqbot.util.UserUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TwoFAMinecraft
{
	private TwoFAMinecraft()
	{
		//Overrides the default, public, constructor
	}

	public static void startTwoFA()
	{
		TaskHandler.addRepeatingTask(() ->
		{
			for(String selectedID : getPending())
			{
				sendDirectMessage(selectedID);
			}
		}, "2faPending", 0, TimeUnit.SECONDS, 5);
	}

	private static void sendDirectMessage(String id)
	{
		User user = UserUtils.getUserFromMention(id);
		if(user != null)
		{
			String code = generateCode();
			user.openPrivateChannel().queue(
					channel ->
					{
						EmbedGenerator embed = new EmbedGenerator(channel)
								.text("Here is your Minecraft 2FA Code: `" + code + "`\n If you did not request this code, please ignore this message.")
								.color(EmbedUtils.IGSQ_PURPLE);

						channel.sendMessage(embed.getBuilder().build()).queue(
								message ->
								{
									Database.updateCommand("UPDATE discord_2fa SET code = '" + code + "' WHERE uuid = '" + CommonMinecraft.getUUIDFromID(id) + "';");

									TaskHandler.addTask(() ->
									{
										new EmbedGenerator(message.getEmbeds().get(0)).text("Here is your Minecraft 2FA Code: **EXPIRED**\n If you did not request this code, please ignore this message.").replace(message);
										if(Database.scalarCommand("SELECT COUNT(*) FROM discord_2fa WHERE uuid = '" + CommonMinecraft.getUUIDFromID(id) + "' AND current_status = 'pending';") > 0)
										{
											Database.updateCommand("UPDATE discord_2fa SET current_status = 'expired' WHERE uuid = '" + CommonMinecraft.getUUIDFromID(id) + "';");
										}
										Database.updateCommand("UPDATE discord_2fa SET code = NULL WHERE uuid = '" + CommonMinecraft.getUUIDFromID(id) + "';");
									}, TimeUnit.SECONDS, 60);
								}, error -> {}
						);
					}
			);
		}
	}

	private static String generateCode()
	{
		return String.format("%06d", new Random().nextInt(999999));
	}

	private static List<String> getPending()
	{
		ResultSet discord_2fa = Database.queryCommand("SELECT * FROM discord_2fa WHERE current_status = 'pending' AND `code` IS NULL");
		List<String> pendingIDs = new ArrayList<>();

		if(discord_2fa == null)
		{
			return pendingIDs;
		}
		try
		{
			while(discord_2fa.next())
			{
				pendingIDs.add(CommonMinecraft.getIDFromUUID(discord_2fa.getString(1)));
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
		return pendingIDs;
	}
}
