package org.igsq.igsqbot.setup;

import java.awt.Color;

import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.GUIGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Setup_Command 
{
	private final MessageChannel channel;
	private final User author;
	private final Member member;
	private final String[] args;
	public Setup_Command(MessageReceivedEvent event)
	{
		this.author = event.getAuthor();
		this.channel = event.getChannel();
		this.member = event.getMember();
		this.args = event.getMessage().getContentRaw().toLowerCase().split(" ");
		setupQuery();
	}
	
	private void setupQuery()
	{
		if(!author.isBot() && member.hasPermission(Permission.MESSAGE_MANAGE)) setup();
		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
	}
	
	private void setup()
	{
		String action = "";
		try
		{
			action = args[0];
		}
		catch(Exception exception) 
		{
			new EmbedGenerator(channel).text("You entered an invalid action").sendTemporary(); 
			return;
		}
		
		switch(action.toLowerCase()) 
		{
//			case "verify":
//				new Verification_Setup(event);
//				break;
			default:
				GUIGenerator menu = new GUIGenerator(new EmbedGenerator(channel).text("Are you gay?\n:one: Yes\n:two: yes but lowercase")); 
				int option = menu.menu(author, 10000, 2);
				if(option == -1)
				{
					new EmbedGenerator(channel).text("SHIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIT").replace(menu.getMessage());
				}
				else if(option == 1)
				{
					new EmbedGenerator(channel).text("Are you gay2?\n:one: Yes2\n:two: yes but lowercase2\n:three: NO\n:four: no but less mad\n:five: banana").replace(menu.getMessage());
					int option2 = menu.menu(author, 10000, 5);
					if(option2 == 1)
					{
						new EmbedGenerator(channel).text("KankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKankerKanker").replace(menu.getMessage());
					}
				}
				else if(option == 2)
				{
					new EmbedGenerator(channel).text("wow, pretty gay! but in lowercase").replace(menu.getMessage());
				}
		}
	}
}

