//package org.igsq.igsqbot.commands;
//
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.entities.MessageChannel;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import org.igsq.igsqbot.Common;
//import org.igsq.igsqbot.handlers.CooldownHandler;
//import org.igsq.igsqbot.objects.EmbedGenerator;
//import org.igsq.igsqbot.objects.ErrorHandler;
//
//import java.awt.Color;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class Ping_Command
//{
//	private final User author;
//	private final MessageChannel channel;
//	private final JDA jda;
//	private final CooldownHandler cooldownHandler;
//
//	public Ping_Command(MessageReceivedEvent event)
//	{
//		this.author = event.getAuthor();
//		this.channel = event.getChannel();
//		this.jda = event.getJDA();
//		pingQuery();
//	}
//
//	private void pingQuery()
//	{
//		if(!author.isBot()) ping();
//		else new EmbedGenerator(channel).text("You cannot Execute this command!\nThis may be due to sending it in the wrong channel or not having the required permission.").color(Color.RED).sendTemporary();
//	}
//
//	private void ping()
//	{
//		if(CooldownHandler.isOnCooldown(author.getIdLong(), this))
//		{
//			new EmbedGenerator(channel).color(Color.RED).text("This command is on cooldown. " + cooldownHandler.getCooldown("ping") / 1000 + " seconds left").sendTemporary();
//			return;
//		}
//		cooldownHandler.createCooldown("ping", 10000);
//		EmbedGenerator embed = new EmbedGenerator(channel).color(Color.ORANGE).title("Pong!").footer(Common.getTimestamp());
//
//		jda.getRestPing().queue(
//			time ->
//			{
//				embed.text("**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms");
//				channel.sendMessage(embed.getBuilder().build()).queue(
//						message ->
//						{
//							new EmbedGenerator(null).text(
//									"**REST Ping**: " + time + "ms\n**Gateway Ping**: " + jda.getGatewayPing() + "ms"
//									+ "\n\n**30 Second REST Average**: " + getAverageREST(30) + "ms"
//									+ "\n**1 Minute REST Average**: " + getAverageREST(60) + "ms")
//
//									.color(Color.ORANGE)
//									.title("Pong!")
//									.footer(Common.getTimestamp())
//									.replace(message);
//						});
//			}
//		);
//	}
//
//	private String getAverageREST(int seconds)
//	{
//		AtomicLong averagePing = new AtomicLong();
//
//		ScheduledFuture<?> collectAverages = Common.scheduler.scheduleAtFixedRate(() -> jda.getRestPing().queueAfter(5, TimeUnit.SECONDS, averagePing::addAndGet), 0, 5000, TimeUnit.MILLISECONDS);
//		ScheduledFuture<?> stopCollection = Common.scheduler.schedule(() -> collectAverages.cancel(false), seconds, TimeUnit.SECONDS);
//
//		try
//		{
//			while(!stopCollection.isDone())
//			{
//				synchronized(this)
//				{
//					wait(100);
//				}
//			}
//			return "" + averagePing.get() / (seconds / 5);
//		}
//		catch(Exception exception)
//		{
//			new ErrorHandler(exception);
//			return "An error occurred while collecting the averages.";
//		}
//	}
//}
