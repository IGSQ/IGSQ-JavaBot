package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.Cooldown_Handler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Main_Command extends ListenerAdapter
{
	private static Cooldown_Handler[] cooldownHandlers = {};
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
    		
    		if(getHandler(event.getGuild()) == null) cooldownHandlers = Common.append(cooldownHandlers, new Cooldown_Handler(event.getGuild()));

    		event.getMessage().delete().queue();
    		
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
	        		Common.sendEmbed("Command " + command + " not found.", (TextChannel) event.getChannel(),Color.RED);
	        		break;
        	}
        }
    }
    
    public static Cooldown_Handler getHandler(Guild guild)
    {
    	for(Cooldown_Handler selectedHandler : cooldownHandlers) if(selectedHandler.getGuild().equals(guild)) return selectedHandler;
		return null;
    }
    
    public static void removeHandler(Cooldown_Handler handler)
    {
    	cooldownHandlers = Common.depend(cooldownHandlers, handler);
    }
}
