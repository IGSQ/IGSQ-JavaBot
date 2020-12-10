package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.handlers.CooldownHandler;
import org.igsq.igsqbot.improvedcommands.Modhelp_Command;
import org.igsq.igsqbot.objects.EmbedGenerator;
import org.igsq.igsqbot.setup.Setup_Command;
import org.igsq.igsqbot.objects.EventWaiter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.ChannelType;
import org.igsq.igsqbot.util.Command_Utils;

public class Main_Command extends ListenerAdapter
{
	private static CooldownHandler[] cooldownHandlers = {};
	
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
    	if(!event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(Common.BOT_PREFIX) && !EventWaiter.waitingOnThis(event))
    	{
    		final String command = event.getMessage().getContentRaw().toLowerCase().split(" ")[0].substring(1);
//    		args = event.getMessage().getContentRaw().toLowerCase().split(" ");
//    		slashArgs = Common.removeBeforeCharacter(event.getMessage().getContentRaw(), ' ').split("/");
//    		mentionDescriptiveArgs = event.getMessage().getContentRaw().toLowerCase().split(" ", 3);
//    		descriptiveArgs = event.getMessage().getContentRaw().toLowerCase().split(" ", 2);

//    		args = Common.depend(args, 0);
//    		mentionDescriptiveArgs = Common.depend(mentionDescriptiveArgs, 0);
//    		descriptiveArgs = Common.depend(descriptiveArgs, 0);
    		
    		String id = null;
    		if(event.getChannelType().equals(ChannelType.TEXT)) 
    		{	
        		event.getMessage().delete().queue();
        		id = event.getGuild().getId();
    		}
    		else id = event.getChannel().getId();
    		// if(getHandler(id) == null) cooldownHandlers = Common_Command.append(cooldownHandlers, new CooldownHandler(id));

    		Common.commandExecutor.submit(() ->
		    {
	            switch(command)
	            {

		            case "shutdown":
			            new Shutdown_Command(event);
			            break;

//		            case "clear":
//			            new Clear_Command(event);
//			            break;

		            case "verify":
		            case "v":
		            case "accept":
			            new Verify_Command(event);
			            break;

		            case "test":
			            new Test_Command(event);
			            break;
		            case "setup":
			            new Setup_Command(event);
			            break;

//		            case "ping":
//		            	new Ping_Command(event);
//						break;
		            default:
			            new EmbedGenerator(event.getChannel()).text("Command " + command + " not found.").color(Color.RED).sendTemporary();
			            break;
	            }

		    });
        }
    }
    
//    public static CooldownHandler getHandler(String id)
//    {
//    	for(CooldownHandler selectedHandler : cooldownHandlers) if(selectedHandler.getId().equals(id)) return selectedHandler;
//		return null;
//    }
    
    public static void removeHandler(CooldownHandler handler)
    {
    	cooldownHandlers = Command_Utils.depend(cooldownHandlers, handler);
    }
}
