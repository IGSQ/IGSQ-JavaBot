package org.igsq.igsqbot;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;

public class Embed {
	private EmbedBuilder embed;
	private MessageChannel channel;
	private String[] reactions = {};
	
	public Embed(MessageChannel channel)
	{
		this.channel = channel;
		embed = new EmbedBuilder();
	}
	
	public Embed footer(String text) 
	{
		embed.setFooter(text);
		return this;
	}
	public Embed footer(String text,String iconUrl) 
	{
		embed.setFooter(text, iconUrl);
		return this;
	}
	
	public Embed title(String text) 
	{
		embed.setTitle(text);
		return this;
	}
	public Embed title(String text,String iconUrl) 
	{
		embed.setTitle(text, iconUrl);
		return this;
	}
	
	public Embed text(String text) 
	{
		embed.setDescription(text);
		return this;
	}
	
	public Embed element(String subTitle,String description) 
	{
		embed.addField(subTitle, description, false);
		return this;
	}
	public Embed element(String subTitle,String description,boolean inline) 
	{
		embed.addField(subTitle, description, inline);
		return this;
	}
	public Embed element(Field field) 
	{
		embed.addField(field);
		return this;
	}
	
	public Embed element(Field[] field) 
	{
		for(Field selectedField : field) embed.addField(selectedField);
		return this;
	}
	
	public Embed color(Color color) 
	{
		embed.setColor(color);
		return this;
	}
	
	public Embed thumbnail(String url) 
	{
		embed.setThumbnail(url);
		return this;
	}
	public Embed image(String url) 
	{
		embed.setImage(url);
		return this;
	}
	
	
	public Embed reaction(String emojiUnicode) 
	{
		reactions = Common.append(reactions, emojiUnicode);
		return this;
	}
	
	public Embed reaction(String[] emojiUnicodes) 
	{
		reactions = Common.append(reactions, emojiUnicodes);
		return this;
	}
	
	public Embed author(String author) 
	{
		embed.setAuthor(author);
		return this;
	}
	public Embed author(String author,String url) 
	{
		embed.setAuthor(author, url);
		return this;
	}
	public Embed author(String author,String url,String iconUrl) 
	{
		embed.setAuthor(author, url, iconUrl);
		return this;
	}
	
	public void send()
	{
		if(embed.isEmpty()) return;
		if(channel instanceof TextChannel && !((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) return;
        channel.sendMessage(embed.build()).queue
        (
				message -> 
				{
					if(channel instanceof TextChannel && ((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) for(String reaction : reactions) message.addReaction(reaction).queue();
				}
		);
	}
	
	public void sendTemporary(int delay) 
	{
		if(embed.isEmpty()) return;
		if(channel instanceof TextChannel && !((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) return;
		if(delay < 0) return;
		if(delay == 0) 
		{
			send();
			return;
		}
		channel.sendMessage(embed.build()).queue
		(
    		message -> 
    		{
    			if(channel instanceof TextChannel && ((TextChannel)channel).getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) for(String reaction : reactions) message.addReaction(reaction).queue();
    			message.delete().submitAfter(delay, TimeUnit.MILLISECONDS);
    		}
        );
	}
	public void sendTemporary() 
	{
		sendTemporary(10000);
	}
	
	
	public String[] getReactions() 
	{
		return reactions;
	}
	public MessageChannel getChannel() 
	{
		return channel;
	}
	public EmbedBuilder getBuilder() 
	{
		return embed;
	}
	
}
