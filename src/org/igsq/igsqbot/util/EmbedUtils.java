package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.MessageChannel;
import org.igsq.igsqbot.Common;
import org.igsq.igsqbot.objects.EmbedGenerator;

import java.awt.*;

public class EmbedUtils
{
	public static final int CHARACTER_LIMIT = 2048;
	public static final int REACTION_LIMIT = 20;
	public static final int EMBED_TITLE_LIMIT = 256;
	public static final Color IGSQ_PURPLE = new Color(104, 89, 133);

	private EmbedUtils()
	{
		//Overrides the default, public, constructor
	}

	public static void sendError(MessageChannel channel, String errorText)
	{
		new EmbedGenerator(channel).text(errorText).color(Color.RED).sendTemporary().addReaction(Common.TICK_REACTIONS.get(1)).queue();
	}

}
