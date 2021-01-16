/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq;


import org.igsq.igsqbot.entities.jooq.tables.*;
import org.igsq.igsqbot.entities.jooq.tables.records.*;
import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Keys
{

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<GuildsRecord> GUILDS_PKEY = Internal.createUniqueKey(Guilds.GUILDS, DSL.name("guilds_pkey"), new TableField[]{Guilds.GUILDS.GUILDID}, true);
    public static final UniqueKey<MutesRecord> MUTES_GUILDID_KEY = Internal.createUniqueKey(Mutes.MUTES, DSL.name("mutes_guildid_key"), new TableField[]{Mutes.MUTES.GUILDID}, true);
    public static final UniqueKey<MutesRecord> MUTES_PKEY = Internal.createUniqueKey(Mutes.MUTES, DSL.name("mutes_pkey"), new TableField[]{Mutes.MUTES.ID}, true);
    public static final UniqueKey<MutesRecord> MUTES_USERID_KEY = Internal.createUniqueKey(Mutes.MUTES, DSL.name("mutes_userid_key"), new TableField[]{Mutes.MUTES.USERID}, true);
    public static final UniqueKey<ReactionRolesRecord> REACTION_ROLES_PKEY = Internal.createUniqueKey(ReactionRoles.REACTION_ROLES, DSL.name("reaction_roles_pkey"), new TableField[]{ReactionRoles.REACTION_ROLES.ID}, true);
    public static final UniqueKey<RemindersRecord> REMINDERS_PKEY = Internal.createUniqueKey(Reminders.REMINDERS, DSL.name("reminders_pkey"), new TableField[]{Reminders.REMINDERS.ID}, true);
    public static final UniqueKey<ReportsRecord> REPORTS_MESSAGEID_KEY = Internal.createUniqueKey(Reports.REPORTS, DSL.name("reports_messageid_key"), new TableField[]{Reports.REPORTS.MESSAGEID}, true);
    public static final UniqueKey<ReportsRecord> REPORTS_PKEY = Internal.createUniqueKey(Reports.REPORTS, DSL.name("reports_pkey"), new TableField[]{Reports.REPORTS.ID}, true);
    public static final UniqueKey<ReportsRecord> REPORTS_REPORTMESSAGEID_KEY = Internal.createUniqueKey(Reports.REPORTS, DSL.name("reports_reportmessageid_key"), new TableField[]{Reports.REPORTS.REPORTMESSAGEID}, true);
    public static final UniqueKey<RolesRecord> ROLES_PKEY = Internal.createUniqueKey(Roles.ROLES, DSL.name("roles_pkey"), new TableField[]{Roles.ROLES.ID}, true);
    public static final UniqueKey<VotesRecord> VOTES_PKEY = Internal.createUniqueKey(Votes.VOTES, DSL.name("votes_pkey"), new TableField[]{Votes.VOTES.ID}, true);
    public static final UniqueKey<WarningsRecord> WARNINGS_PKEY = Internal.createUniqueKey(Warnings.WARNINGS, DSL.name("warnings_pkey"), new TableField[]{Warnings.WARNINGS.WARNID}, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<MutesRecord, GuildsRecord> MUTES__MUTES_GUILDID_FKEY = Internal.createForeignKey(Mutes.MUTES, DSL.name("mutes_guildid_fkey"), new TableField[]{Mutes.MUTES.GUILDID}, Keys.GUILDS_PKEY, new TableField[]{Guilds.GUILDS.GUILDID}, true);
    public static final ForeignKey<ReactionRolesRecord, GuildsRecord> REACTION_ROLES__REACTION_ROLES_GUILDID_FKEY = Internal.createForeignKey(ReactionRoles.REACTION_ROLES, DSL.name("reaction_roles_guildid_fkey"), new TableField[]{ReactionRoles.REACTION_ROLES.GUILDID}, Keys.GUILDS_PKEY, new TableField[]{Guilds.GUILDS.GUILDID}, true);
    public static final ForeignKey<RemindersRecord, GuildsRecord> REMINDERS__REMINDERS_GUILDID_FKEY = Internal.createForeignKey(Reminders.REMINDERS, DSL.name("reminders_guildid_fkey"), new TableField[]{Reminders.REMINDERS.GUILDID}, Keys.GUILDS_PKEY, new TableField[]{Guilds.GUILDS.GUILDID}, true);
    public static final ForeignKey<ReportsRecord, GuildsRecord> REPORTS__REPORTS_GUILDID_FKEY = Internal.createForeignKey(Reports.REPORTS, DSL.name("reports_guildid_fkey"), new TableField[]{Reports.REPORTS.GUILDID}, Keys.GUILDS_PKEY, new TableField[]{Guilds.GUILDS.GUILDID}, true);
    public static final ForeignKey<RolesRecord, GuildsRecord> ROLES__ROLES_GUILDID_FKEY = Internal.createForeignKey(Roles.ROLES, DSL.name("roles_guildid_fkey"), new TableField[]{Roles.ROLES.GUILDID}, Keys.GUILDS_PKEY, new TableField[]{Guilds.GUILDS.GUILDID}, true);
    public static final ForeignKey<WarningsRecord, GuildsRecord> WARNINGS__WARNINGS_GUILDID_FKEY = Internal.createForeignKey(Warnings.WARNINGS, DSL.name("warnings_guildid_fkey"), new TableField[]{Warnings.WARNINGS.GUILDID}, Keys.GUILDS_PKEY, new TableField[]{Guilds.GUILDS.GUILDID}, true);
}
