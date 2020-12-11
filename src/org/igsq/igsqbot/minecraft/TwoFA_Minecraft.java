package org.igsq.igsqbot.minecraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.*;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.igsq.igsqbot.util.Array_Utils;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.util.String_Utils;
import org.igsq.igsqbot.util.User_Utils;
import org.igsq.igsqbot.util.Yaml_Utils;

public class TwoFA_Minecraft 
{
	private TwoFA_Minecraft()
	{
		//Overrides the default, public, constructor
	}
	
	public static void startTwoFA()
	{
		Common.scheduler.scheduleAtFixedRate(() ->
		{
			for(String selectedID : getPending())
			{
				sendDirectMessage(selectedID);
			}
		}, 0, 5,TimeUnit.SECONDS);
	}
	
	private static void sendDirectMessage(String id)
	{
		User user = User_Utils.getUserFromMention(id);
		if(user != null)
		{
			String code = generateCode();
			user.openPrivateChannel().queue(
				channel ->
				{
					EmbedGenerator embed = new EmbedGenerator(channel)
							.text("Here is your Minecraft 2FA Code: `" + code + "`\n If you did not request this code, please ignore this message.")
							.color(Common.IGSQ_PURPLE);
					channel.sendMessage(embed.getBuilder().build()).queue(
							message ->
							{
								Database.updateCommand("UPDATE discord_2fa SET code = '" + code +  "' WHERE uuid = '" + Common_Minecraft.getUUIDFromID(id) + "';");

								Common.scheduler.schedule(() ->
								{
									new EmbedGenerator(message.getEmbeds().get(0)).text("Here is your Minecraft 2FA Code: **EXPIRED**\n If you did not request this code, please ignore this message.").replace(message);
									if(Database.scalarCommand("SELECT COUNT(*) FROM discord_2fa WHERE uuid = '" + Common_Minecraft.getUUIDFromID(id) + "' AND current_status = 'pending';") > 0)
									{
										Database.updateCommand("UPDATE discord_2fa SET current_status = 'expired' WHERE uuid = '" + Common_Minecraft.getUUIDFromID(id) + "';");
									}
									Database.updateCommand("UPDATE discord_2fa SET code = NULL WHERE uuid = '" + Common_Minecraft.getUUIDFromID(id) + "';");

								}, 60, TimeUnit.SECONDS);
							}
					);
				}
			);
		}
	}
			
	private static String generateCode()
	{
		return String.format("%06d", new Random().nextInt(999999));
	}
	
	private static String[] getPending()
	{
		ResultSet discord_2fa = Database.queryCommand("SELECT * FROM discord_2fa WHERE current_status = 'pending' AND `code` IS NULL");
		String[] pendingIDs = new String[0];
		try 
		{
			while(discord_2fa.next())
			{
				pendingIDs = Array_Utils.append(pendingIDs, Common_Minecraft.getIDFromUUID(discord_2fa.getString(1)));
			}
		} 
		catch (Exception exception)
		{
			new ErrorHandler(exception);
		}
		return pendingIDs;
	}
}
