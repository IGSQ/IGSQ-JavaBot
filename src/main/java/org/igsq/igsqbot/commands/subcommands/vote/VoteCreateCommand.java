package org.igsq.igsqbot.commands.subcommands.vote;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.igsq.igsqbot.entities.command.Command;
import org.igsq.igsqbot.entities.command.CommandContext;
import org.igsq.igsqbot.entities.database.Vote;
import org.igsq.igsqbot.entities.exception.SyntaxException;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.CommandChecks;
import org.igsq.igsqbot.util.Parser;

public class VoteCreateCommand extends Command
{
    public VoteCreateCommand(Command parent)
    {
        super(parent, "create", "Creates a vote.", "[role1/role2{6}][option1/option2{3}][duration][subject]");
        addMemberPermissions(Permission.MANAGE_CHANNEL);
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(List<String> args, CommandContext ctx)
    {
        CommandChecks.argsSizeSubceeds(ctx, 3);

        List<String> options = new Parser(args.get(1), ctx).parseAsSlashArgs();
        LocalDateTime expiry = new Parser(args.get(2), ctx).parseAsDuration();
        String subject = ArrayUtils.arrayCompile(args.subList(3, args.size()), " ");
        Guild guild = ctx.getGuild();
        List<Role> roles = new ArrayList<>();
        List<Long> users = new ArrayList<>();

        int argCount = new Parser(args.get(0), ctx).parseAsSlashArgs().size();
        int roleCount = ctx.getMessage().getMentionedRoles().size();
        int memberCount = ctx.getMessage().getMentionedMembers().size();

        if(roleCount == argCount)
        {
            roles = ctx.getMessage().getMentionedRoles();
        }
        else if(memberCount == argCount)
        {
            users = ctx.getMessage().getMentionedUsers().stream().map(User::getIdLong).collect(Collectors.toList());
        }
        else
        {
            roles = new Parser(args.get(0), ctx)
                    .parseAsSlashArgs()
                    .stream()
                    .map(arg -> arg.replaceAll("[^a-z]/gi", ""))
                    .map(guild::getRoleById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());


            users = new Parser(args.get(0), ctx)
                    .parseAsSlashArgs()
                    .stream()
                    .map(arg -> arg.replaceAll("[^a-z]/gi", ""))
                    .map(guild::getMemberById)
                    .filter(Objects::nonNull)
                    .map(Member::getIdLong)
                    .collect(Collectors.toList());
        }


        if(options.isEmpty() || options.size() > 6 || expiry == null || roles.size() > 3 || users.size() > 10)
        {
            throw new SyntaxException(ctx);
        }

        if(users.isEmpty() && !roles.isEmpty())
        {
            List<Role> finalRoles = roles;
            guild.findMembers(member -> member.getRoles().stream().anyMatch(finalRoles::contains)).onSuccess(
                    members ->
                    {
                        if(members.isEmpty())
                        {
                            ctx.replyError("No members found for roles " + finalRoles
                                    .stream()
                                    .map(Role::getAsMention)
                                    .collect(Collectors.joining(" ")));
                            return;
                        }

                        if(members.size() > 20)
                        {
                            ctx.replyError("Too many members found for roles " + finalRoles
                                    .stream()
                                    .map(Role::getAsMention)
                                    .collect(Collectors.joining(" ")));
                            return;
                        }

                        Vote vote = new Vote(members.stream().map(Member::getIdLong).collect(Collectors.toList()), options, expiry, subject, ctx);
                        vote.start();
                    });
        }
        else if(roles.isEmpty() && !users.isEmpty())
        {
            Vote vote = new Vote(users, options, expiry, subject, ctx);
            vote.start();
        }
        else
        {
            throw new SyntaxException(ctx);
        }
    }
}