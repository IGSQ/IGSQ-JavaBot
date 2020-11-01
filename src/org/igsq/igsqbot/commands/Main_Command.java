package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;

public class Main_Command extends ListenerAdapter
{
	private static Cooldown_Handler[] cooldownHandlers = {};
	public Main_Command()
	{
		Common.jdaBuilder.addEventListeners(this);
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
    		
    		if(getHandler(event.getGuild()) == null) cooldownHandlers = Common_Command.append(cooldownHandlers, new Cooldown_Handler(event.getGuild()));

    		event.getMessage().delete().complete();
    		
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
	        	case "s":
	        		new Shutdown_Command(event);
	        		break;
	        		
	        	case "clear":
	        		new Clear_Command(event, args);
	        		break;
	        	
	        	case "verify":
	        	case "v":
	        		new Verify_Command(event);
	        		break;
	        	case "match":
	        	case "m":
	        		new Match_Command(event,slashArgs);
	        		break;
	        		
	        	case "report":
	        		new Report_Command(event, args);
	        		break;
	        		
	        	default:
	        		new EmbedGenerator(event.getChannel()).text("Command " + command + " not found.").color(Color.RED).sendTemporary();
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
    	cooldownHandlers = Common_Command.depend(cooldownHandlers, handler);
    }
}
