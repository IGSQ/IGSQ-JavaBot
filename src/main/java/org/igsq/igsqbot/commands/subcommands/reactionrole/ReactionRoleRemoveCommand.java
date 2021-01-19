package org.igsq.igsqbot.commands.subcommands.reactionrole;

import java.util.List;
import java.util.OptionalLong;
import net.dv8tion.jda.api.Permission;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.command.CommandFlag;
import org.igsq.igsqbot.entities.database.ReactionRole;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;
import org.igsq.igsqbot.util.StringUtils;

public class ReactionRoleRemoveCommand extends Command
{
	public ReactionRoleRemoveCommand(Command parent)
	{
		super(parent, "remove", "Removes reaction roles.", "[messageId][channel][role][emote]");
		addFlags(CommandFlag.GUILD_ONLY);
		addMemberPermissions(Permission.MANAGE_SERVER);
		addSelfPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	public void run(List<String> args, CommandContext ctx)
	{
		CommandChecks.argsSizeSubceeds(ctx, 4);

		OptionalLong messageId = new Parser(args.get(0), ctx).parseAsUnsignedLong();
		String emote = ctx.getMessage().getEmotes().isEmpty() ? args.get(3) : ctx.getMessage().getEmotes().get(0).getId();
		new Parser(args.get(2), ctx).parseAsRole(role ->
		{
			if(messageId.isPresent())
			{
				new Parser(args.get(1), ctx).parseAsTextChannel(
						channel ->
						{
							channel.retrieveMessageById(messageId.getAsLong()).queue(
									message ->
									{
										ReactionRole reactionRole = new ReactionRole(messageId.getAsLong(), role.getIdLong(), ctx.getGuild().getIdLong(), emote, ctx.getIGSQBot());

										if(!reactionRole.isPresent())
										{
											throw new IllegalArgumentException("That reaction role does not exist");
										}

										reactionRole.remove();
										ctx.replySuccess("Removed reaction role for role " + StringUtils.getRoleAsMention(role.getIdLong()));
										message.clearReactions(emote).queue();
									},
									error -> ctx.replyError("That message does not exist"));
						});
			}
		});
	}
}
