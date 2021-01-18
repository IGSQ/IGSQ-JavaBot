/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.generated.tables.Blacklists;
import org.jooq.generated.tables.Guilds;
import org.jooq.generated.tables.Levels;
import org.jooq.generated.tables.ReactionRoles;
import org.jooq.generated.tables.Reminders;
import org.jooq.generated.tables.Reports;
import org.jooq.generated.tables.Roles;
import org.jooq.generated.tables.Tempbans;
import org.jooq.generated.tables.Votes;
import org.jooq.generated.tables.Warnings;
import org.jooq.generated.tables.records.BlacklistsRecord;
import org.jooq.generated.tables.records.GuildsRecord;
import org.jooq.generated.tables.records.LevelsRecord;
import org.jooq.generated.tables.records.ReactionRolesRecord;
import org.jooq.generated.tables.records.RemindersRecord;
import org.jooq.generated.tables.records.ReportsRecord;
import org.jooq.generated.tables.records.RolesRecord;
import org.jooq.generated.tables.records.TempbansRecord;
import org.jooq.generated.tables.records.VotesRecord;
import org.jooq.generated.tables.records.WarningsRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<BlacklistsRecord> BLACKLISTS_PKEY = Internal.createUniqueKey(Blacklists.BLACKLISTS, DSL.name("blacklists_pkey"), new TableField[] { Blacklists.BLACKLISTS.ID }, true);
    public static final UniqueKey<GuildsRecord> GUILDS_PKEY = Internal.createUniqueKey(Guilds.GUILDS, DSL.name("guilds_pkey"), new TableField[] { Guilds.GUILDS.GUILD_ID }, true);
    public static final UniqueKey<LevelsRecord> LEVELS_PKEY = Internal.createUniqueKey(Levels.LEVELS, DSL.name("levels_pkey"), new TableField[] { Levels.LEVELS.ID }, true);
    public static final UniqueKey<ReactionRolesRecord> REACTION_ROLES_PKEY = Internal.createUniqueKey(ReactionRoles.REACTION_ROLES, DSL.name("reaction_roles_pkey"), new TableField[] { ReactionRoles.REACTION_ROLES.ID }, true);
    public static final UniqueKey<RemindersRecord> REMINDERS_PKEY = Internal.createUniqueKey(Reminders.REMINDERS, DSL.name("reminders_pkey"), new TableField[] { Reminders.REMINDERS.ID }, true);
    public static final UniqueKey<ReportsRecord> REPORTS_MESSAGE_ID_KEY = Internal.createUniqueKey(Reports.REPORTS, DSL.name("reports_message_id_key"), new TableField[] { Reports.REPORTS.MESSAGE_ID }, true);
    public static final UniqueKey<ReportsRecord> REPORTS_PKEY = Internal.createUniqueKey(Reports.REPORTS, DSL.name("reports_pkey"), new TableField[] { Reports.REPORTS.ID }, true);
    public static final UniqueKey<ReportsRecord> REPORTS_REPORT_MESSAGE_ID_KEY = Internal.createUniqueKey(Reports.REPORTS, DSL.name("reports_report_message_id_key"), new TableField[] { Reports.REPORTS.REPORT_MESSAGE_ID }, true);
    public static final UniqueKey<RolesRecord> ROLES_PKEY = Internal.createUniqueKey(Roles.ROLES, DSL.name("roles_pkey"), new TableField[] { Roles.ROLES.ID }, true);
    public static final UniqueKey<TempbansRecord> TEMPBANS_GUILD_ID_KEY = Internal.createUniqueKey(Tempbans.TEMPBANS, DSL.name("tempbans_guild_id_key"), new TableField[] { Tempbans.TEMPBANS.GUILD_ID }, true);
    public static final UniqueKey<TempbansRecord> TEMPBANS_PKEY = Internal.createUniqueKey(Tempbans.TEMPBANS, DSL.name("tempbans_pkey"), new TableField[] { Tempbans.TEMPBANS.ID }, true);
    public static final UniqueKey<TempbansRecord> TEMPBANS_USER_ID_KEY = Internal.createUniqueKey(Tempbans.TEMPBANS, DSL.name("tempbans_user_id_key"), new TableField[] { Tempbans.TEMPBANS.USER_ID }, true);
    public static final UniqueKey<VotesRecord> VOTES_PKEY = Internal.createUniqueKey(Votes.VOTES, DSL.name("votes_pkey"), new TableField[] { Votes.VOTES.ID }, true);
    public static final UniqueKey<WarningsRecord> WARNINGS_PKEY = Internal.createUniqueKey(Warnings.WARNINGS, DSL.name("warnings_pkey"), new TableField[] { Warnings.WARNINGS.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<BlacklistsRecord, GuildsRecord> BLACKLISTS__BLACKLISTS_GUILD_ID_FKEY = Internal.createForeignKey(Blacklists.BLACKLISTS, DSL.name("blacklists_guild_id_fkey"), new TableField[] { Blacklists.BLACKLISTS.GUILD_ID }, Keys.GUILDS_PKEY, new TableField[] { Guilds.GUILDS.GUILD_ID }, true);
    public static final ForeignKey<TempbansRecord, GuildsRecord> TEMPBANS__TEMPBANS_GUILD_ID_FKEY = Internal.createForeignKey(Tempbans.TEMPBANS, DSL.name("tempbans_guild_id_fkey"), new TableField[] { Tempbans.TEMPBANS.GUILD_ID }, Keys.GUILDS_PKEY, new TableField[] { Guilds.GUILDS.GUILD_ID }, true);
}
