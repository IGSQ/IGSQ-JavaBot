package org.igsq.igsqbot.minecraft;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Database;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.Yaml;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class TwoFA_Minecraft 
{
	public TwoFA_Minecraft()
	{
		TwoFA();
	}
	
	private void TwoFA()
	{
		Common.scheduler.scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run() 
			{
				for(String selectedID : getPending())
				{
					sendDirectMessage(selectedID);
				}
			} 		
		}, 0, 5,TimeUnit.SECONDS);
	}
	
	private void sendDirectMessage(String id)
	{
		User user = Common.getUserFromMention(id);
		if(user != null && (Common.isFieldEmpty("2fa.blacklist", "internal") || !Common.isValueInArray(Yaml.getFieldString("2fa.blacklist", "internal").split(","), id)))
		{
			if(Common.isFieldEmpty("2fa.blacklist", "internal"))
			{
				Yaml.updateField("2fa.blacklist", "internal", id);
			}
			else
			{
				Yaml.updateField("2fa.blacklist", "internal", Yaml.getFieldString("2fa.blacklist", "internal") + "," + id);
			}
			
			int code = generateCode();
			PrivateChannel channel = user.openPrivateChannel().complete();
			EmbedGenerator embed = new EmbedGenerator(null).text("Here is your Minecraft 2FA Code: " + code + "\n If you did not request this code, please ignore this mesage.");
			Message message = channel.sendMessage(embed.getBuilder().build()).complete();
			
			Database.UpdateCommand("UPDATE discord_2fa SET code = '" + code +  "' WHERE uuid = '" + Common_Minecraft.getUUIDFromID(id) + "';");

			Common.scheduler.schedule(new Runnable()
			{
				@Override
				public void run() 
				{
					new EmbedGenerator(channel).text("Here is your Minecraft 2FA Code: **EXPIRED**\n If you did not request this code, please ignore this mesage.").replace(message);
					if(Database.ScalarCommand("SELECT COUNT(*) FROM discord_2fa WHERE uuid = '" + Common_Minecraft.getUUIDFromID(id) + "' AND current_status = 'pending';") > 0)
					{
						Database.UpdateCommand("UPDATE discord_2fa SET current_status = 'expired'");
					}
					Yaml.updateField("2fa.blacklist", "internal", Common.stringDepend(Yaml.getFieldString("2fa.blacklist", "internal"), id));
					Database.UpdateCommand("UPDATE discord_2fa SET code = null WHERE uuid = '" + Common_Minecraft.getUUIDFromID(id) + "';");
					
					
				} 		
			}, 60, TimeUnit.SECONDS);
		}
	}
			
	private int generateCode()
	{
		Random random = new Random();
		return random.nextInt(9999);
	}
	
	private String[] getPending()
	{
		ResultSet discord_2fa = Database.QueryCommand("SELECT * FROM discord_2fa WHERE current_status = 'pending'");
		String[] pendingIDs = new String[0];
		try 
		{
			while(discord_2fa.next())
			{
				ResultSet linked_accounts = Database.QueryCommand("SELECT * FROM linked_accounts WHERE uuid = '" + discord_2fa.getString(1) + "';");
				if(linked_accounts.next())
				{
					pendingIDs = Common.append(pendingIDs, linked_accounts.getString(3));
				}
				else
				{
					continue;
				}
			}
		} 
		catch (SQLException exception) 
		{
			
		}
		return pendingIDs;
	}
}
