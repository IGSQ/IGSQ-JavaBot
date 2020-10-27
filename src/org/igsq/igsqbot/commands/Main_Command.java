package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.commands.developer.Shutdown_Command;
import org.igsq.igsqbot.commands.fun.Avatar_Command;
import org.igsq.igsqbot.commands.fun.Poll_Command;
import org.igsq.igsqbot.commands.moderation.Clear_Command;
import org.igsq.igsqbot.commands.moderation.Kick_Command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.TextChannel;

public class Main_Command extends ListenerAdapter
{
	public Main_Command()
	{

	}
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
    	if(!event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(Common.BOT_PREFIX))
    	{
    		String command = event.getMessage().getContentRaw().toLowerCase().split(" ")[0];
    		String[] args = event.getMessage().getContentRaw().toLowerCase().split(" ");
    		String[] slashArgs = Common.removeBeforeCharacter(event.getMessage().getContentRaw(), ' ').split("/");
    		
    		args = Common.depend(args, 0);
    		command = command.substring(1);
    		
        	switch(command)
        	{
	        	case "poll":
	        		new Poll_Command(event, slashArgs);
	        		break;
	        		
	        	case "kick":
	        		new Kick_Command(event);
	        		break;
	        		
	        	case "avatar":
	        		new Avatar_Command(event);
	        		break;
	        		
	        	case "shutdown":
	        		new Shutdown_Command(event);
	        		break;
	        		
	        	case "clear":
	        		new Clear_Command(event, args);
	        		break;
	        		
	        	default:
	        		Common.sendEmbed("Command " + command + " not found.", (TextChannel)event.getChannel(),Color.RED);
	        		break;
        	}
        }
    }
}
