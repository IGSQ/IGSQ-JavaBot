package org.igsq.igsqbot.commands;

import java.awt.Color;

import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.EmbedGenerator;
import org.igsq.igsqbot.minecraft.Link_Minecraft;
import org.igsq.igsqbot.setup.Setup_Command;
import org.igsq.igsqbot.util.EventWaiter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.ChannelType;

public class Main_Command extends ListenerAdapter
{
	private static Cooldown_Handler[] cooldownHandlers = {};
	public Main_Command()
	{
		Common.jdaBuilder.addEventListeners(this);
		
		new MessageReactionAddEvent_Report();
		new MessageReactionAddEvent_Help();
		new MessageReactionAddEvent_Verification();
	}
	
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
    		if(getHandler(id) == null) cooldownHandlers = Common_Command.append(cooldownHandlers, new Cooldown_Handler(id));

    		Common.commandExecuter.submit(new Runnable()
    				{
						@Override
						public void run() 
						{
				        	switch(command)
				        	{
					        	case "poll":
					        		new Poll_Command(event);
					        		break;
					        	case "avatar":
					        		new Avatar_Command(event);
					        		break;
					        		
					        	case "shutdown":
					        		new Shutdown_Command(event);
					        		break;
					        		
					        	case "clear":
					        		new Clear_Command(event);
					        		break;
					        		
					        	case "verify":
					        	case "v":
					        	case "accept":
					        		new Verify_Command(event);
					        		break;
					        		
					        	case "test":
					        		new Test_Command(event);
					        		break;
					        		
					        	case "report":
					        		new Report_Command(event);
					        		break;	
					        		
					        	case "suggest":
					        		new Suggestion_Command(event);
					        		break;
					        		
					        	case "help":
					        		new Help_Command(event);
					        		break;
					        		
					        	case "modhelp":
					        		new Modhelp_Command(event);
					        		break;
					        		
					        	case "link":
					        	case "minecraft":
					        	case "mc":
					        		new Link_Minecraft(event);
					        		break;
					        		
					        	case "setup":
					        		new Setup_Command(event);
					        		break;
					        		
//					        	case "alias":
//					        		new Alias_Command(event);
//					        		break;
//					        	case "decline":
//					        		new Decline_Command(event);
//					        		break;
					        		
					        	default:
					        		new EmbedGenerator(event.getChannel()).text("Command " + command + " not found.").color(Color.RED).sendTemporary();
					        		break;
				        	}
							
						}
    		
    				});
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
