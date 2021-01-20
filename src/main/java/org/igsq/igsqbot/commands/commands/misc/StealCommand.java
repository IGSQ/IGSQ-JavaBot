package org.igsq.igsqbot.commands.commands.misc;

import java.util.List;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.entities.exception.CommandResultException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.FileUtils;

@SuppressWarnings("unused")
public class StealCommand extends Command
{
	public StealCommand()
	{
		super("Steal", "Steals an image URL and adds it as an emoji. Emoji names must be A-Z with underscores.", "[name] [imageURL]");
		addFlags(CommandFlag.GUILD_ONLY);
		addAliases("steal");
		addMemberPermissions(Permission.MANAGE_EMOTES);
		addSelfPermissions(Permission.MANAGE_EMOTES);
	}

	@Override
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		System.out.println("sanity");
		if(CommandChecks.argsSizeSubceeds(ctx, 2, failure) || CommandChecks.stringIsURL(args.get(1), ctx, failure)) return;


		if(!args.get(0).matches("([A-Z]|[a-z]|_)\\w+"))
		{
			failure.accept(new CommandInputException("Emoji names must be A-Z with underscores (_)"));
			return;
		}

		System.out.println("args ok");

		Icon icon = FileUtils.getIcon(args.get(1));
		if(icon == null)
		{
			failure.accept(new CommandResultException("The image / gif provided could not be loaded."));
			System.out.println("invalid image");
		}
		else
		{
			ctx.getGuild().createEmote(args.get(0), icon).queue(
					emote -> ctx.replySuccess("Added emote " + emote.getAsMention() + " successfully!"),
					error -> ctx.replyError("An error occurred while adding the emote."));
		}
	}
}