package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.ChannelType;

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
    		String[] mentionDescriptiveArgs = event.getMessage().getContentRaw().toLowerCase().split(" ", 3);
    		
    		
    		args = Common.depend(args, 0);
    		mentionDescriptiveArgs = Common.depend(mentionDescriptiveArgs, 0);
    		command = command.substring(1);
    		String id = null;
    		if(event.getChannelType().equals(ChannelType.TEXT)) 
    		{
        		event.getMessage().delete().queue();
        		id = event.getGuild().getId();
    		}
    		else id = event.getChannel().getId();
    		if(getHandler(id) == null) cooldownHandlers = Common_Command.append(cooldownHandlers, new Cooldown_Handler(id));

    		
        	switch(command)
        	{
	        	case "poll":
	        	case "p":
	        		new Poll_Command(event, slashArgs);
	        		break;
	        		
	        	case "kick":
	        	case "k":
	        	case "boot":
	        		new Kick_Command(event);
	        		break;
	        		
	        	case "avatar":
	        	case "a":
	        		new Avatar_Command(event);
	        		break;
	        		
	        	case "shutdown":
	        	case "s":
	        		new Shutdown_Command(event);
	        		break;
	        		
	        	case "clear":
	        	case "c":
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
	        	case "q":
	        	case "question":
	        	case "query":
	        		new Question_Command(event,mentionDescriptiveArgs);
	        		break;
	        		
	        	case "report":
	        	case "r":
	        		new Report_Command(event, mentionDescriptiveArgs);
	        		break;
	        		
	        	default:
	        		new EmbedGenerator(event.getChannel()).text("Command " + command + " not found.").color(Color.RED).sendTemporary();
	        		break;
        	}
        }
    }
    
    public static Cooldown_Handler getHandler(String id)
    {
    	for(Cooldown_Handler selectedHandler : cooldownHandlers) if(selectedHandler.getId().equals(id)) return selectedHandler;
		return null;
    }
    
    public static void removeHandler(Cooldown_Handler handler)
    {
    	cooldownHandlers = Common_Command.depend(cooldownHandlers, handler);
    }
}
