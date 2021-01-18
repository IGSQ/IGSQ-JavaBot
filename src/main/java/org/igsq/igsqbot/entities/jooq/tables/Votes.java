/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.igsq.igsqbot.entities.jooq.Keys;
import org.igsq.igsqbot.entities.jooq.Public;
import org.igsq.igsqbot.entities.jooq.tables.records.VotesRecord;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Votes extends TableImpl<VotesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.votes</code>
     */
    public static final Votes VOTES = new Votes();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<VotesRecord> getRecordType() {
        return VotesRecord.class;
    }

    /**
     * The column <code>public.votes.id</code>.
     */
    public final TableField<VotesRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.votes.vote_id</code>.
     */
    public final TableField<VotesRecord, Long> VOTE_ID = createField(DSL.name("vote_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.votes.guild_id</code>.
     */
    public final TableField<VotesRecord, Long> GUILD_ID = createField(DSL.name("guild_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.votes.direct_message_id</code>.
     */
    public final TableField<VotesRecord, Long> DIRECT_MESSAGE_ID = createField(DSL.name("direct_message_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.votes.user_id</code>.
     */
    public final TableField<VotesRecord, Long> USER_ID = createField(DSL.name("user_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.votes.option</code>.
     */
    public final TableField<VotesRecord, Integer> OPTION = createField(DSL.name("option"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field("'-1'::integer", SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.votes.max_options</code>.
     */
    public final TableField<VotesRecord, Integer> MAX_OPTIONS = createField(DSL.name("max_options"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.votes.expiry</code>.
     */
    public final TableField<VotesRecord, LocalDateTime> EXPIRY = createField(DSL.name("expiry"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "");

    /**
     * The column <code>public.votes.has_voted</code>.
     */
    public final TableField<VotesRecord, Boolean> HAS_VOTED = createField(DSL.name("has_voted"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("false", SQLDataType.BOOLEAN)), this, "");

    private Votes(Name alias, Table<VotesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Votes(Name alias, Table<VotesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.votes</code> table reference
     */
    public Votes(String alias) {
        this(DSL.name(alias), VOTES);
    }

    /**
     * Create an aliased <code>public.votes</code> table reference
     */
    public Votes(Name alias) {
        this(alias, VOTES);
    }

    /**
     * Create a <code>public.votes</code> table reference
     */
    public Votes() {
        this(DSL.name("votes"), null);
    }

    public <O extends Record> Votes(Table<O> child, ForeignKey<O, VotesRecord> key) {
        super(child, key, VOTES);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public Identity<VotesRecord, Long> getIdentity() {
        return (Identity<VotesRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<VotesRecord> getPrimaryKey() {
        return Keys.VOTES_PKEY;
    }

    @Override
    public List<UniqueKey<VotesRecord>> getKeys() {
        return Arrays.<UniqueKey<VotesRecord>>asList(Keys.VOTES_PKEY);
    }

    @Override
    public Votes as(String alias) {
        return new Votes(DSL.name(alias), this);
    }

    @Override
    public Votes as(Name alias) {
        return new Votes(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Votes rename(String name) {
        return new Votes(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Votes rename(Name name) {
        return new Votes(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, Long, Long, Long, Long, Integer, Integer, LocalDateTime, Boolean> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
