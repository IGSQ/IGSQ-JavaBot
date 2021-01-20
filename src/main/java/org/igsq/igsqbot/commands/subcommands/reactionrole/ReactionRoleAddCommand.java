package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import java.util.function.Consumer;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandEvent;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.entities.exception.CommandException;
import org.igsq.igsqbot.entities.exception.CommandHierarchyException;
import org.igsq.igsqbot.entities.exception.CommandInputException;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;

public class ReactionRoleAddCommand extends Command
{
	public ReactionRoleAddCommand(Command parent)
	{
		super(parent, "add", "Adds a reaction role.", "[messageId][channel][role][emote]");
		addFlags(CommandFlag.GUILD_ONLY);
		addMemberPermissions(Permission.MANAGE_SERVER);
		addSelfPermissions(Permission.MESSAGE_ADD_REACTION);
	}

	@Override
	public void run(List<String> args, CommandEvent ctx, Consumer<CommandException> failure)
	{
		if(CommandChecks.argsSizeSubceeds(ctx, 4, failure)) return;

		String emote;
		if(!ctx.getMessage().getEmotes().isEmpty())
		{
			if(ctx.getMessage().getEmotes().get(0).isAnimated())
			{
				failure.accept(new CommandInputException("Animated emotes are not allowed."));
				return;
			}
			emote = ctx.getMessage().getEmotes().get(0).getId();
		}
		else
		{
			emote = args.get(3);
		}

		OptionalLong messageId = new Parser(args.get(0), ctx).parseAsUnsignedLong();

		if(messageId.isPresent())
		{
			new Parser(args.get(1), ctx).parseAsTextChannel(
					channel ->
					{
						channel.retrieveMessageById(messageId.getAsLong()).queue(
								message ->
								{
									new Parser(args.get(2), ctx).parseAsRole(
											role ->
											{
												if(!ctx.getSelfMember().canInteract(role) || !ctx.getMember().canInteract(role))
												{
													failure.accept(new CommandHierarchyException(this));
													return;
												}

												message.addReaction(emote).queue(
														success ->
														{
															new ReactionRole(message.getIdLong(), role.getIdLong(), ctx.getGuild().getIdLong(), emote, ctx.getIGSQBot()).add();
															ctx.replySuccess("Reaction role added.");
														},
														error -> failure.accept(new CommandInputException("I could not add reaction `" + ctx.getMessage().getEmotes().get(0).getName() + "`")));
											});
								},
								error -> failure.accept(new CommandInputException("Message with ID " + messageId.getAsLong() + " not found in channel " + channel.getAsMention())));
					});
		}
	}
}
