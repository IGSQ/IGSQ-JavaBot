package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.entities.exception.CommandResultException;
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
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsSizeSubceeds(ctx, 4);

		String emote;
		if(!ctx.getMessage().getEmotes().isEmpty())
		{
			if(ctx.getMessage().getEmotes().get(0).isAnimated())
			{
				throw new IllegalArgumentException("Animated emotes are not allowed.");
			}
			emote = ctx.getMessage().getEmotes().get(0).getId();
		}
		else
		{
			emote = args.get(3);
		}

		new Parser(args.get(1), ctx).parseAsTextChannel(
				channel ->
				{
					channel.retrieveMessageById(args.get(0)).queue(
							message ->
							{
								new Parser(args.get(2), ctx).parseAsRole(
									role ->
									{
										if(!ctx.getSelfMember().canInteract(role) || !ctx.getMember().canInteract(role))
										{
											throw new CommandResultException("A hierarchy issue occurred when tried to execute command `" + ctx.getCommand().getName() + "`");
										}

										message.addReaction(emote).queue(
												success ->
												{
													new ReactionRole(message.getIdLong(), role.getIdLong(), ctx.getGuild().getIdLong(), emote, ctx.getIGSQBot()).add();
													ctx.replySuccess("Reaction role added.");
												},
												error -> ctx.replyError("An error occurred while adding the reaction."));
									});
							});
				});

	}
}
