/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import java.time.LocalDateTime;

import org.igsq.igsqbot.entities.jooq.tables.Mutes;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MutesRecord extends UpdatableRecordImpl<MutesRecord> implements Record4<Long, Long, Long, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.mutes.muteid</code>.
     */
    public MutesRecord setMuteid(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.mutes.muteid</code>.
     */
    public Long getMuteid() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.mutes.userid</code>.
     */
    public MutesRecord setUserid(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.mutes.userid</code>.
     */
    public Long getUserid() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.mutes.guildid</code>.
     */
    public MutesRecord setGuildid(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.mutes.guildid</code>.
     */
    public Long getGuildid() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.mutes.muteduntil</code>.
     */
    public MutesRecord setMuteduntil(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.mutes.muteduntil</code>.
     */
    public LocalDateTime getMuteduntil() {
        return (LocalDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Long, Long, LocalDateTime> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, Long, Long, LocalDateTime> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Mutes.MUTES.MUTEID;
    }

    @Override
    public Field<Long> field2() {
        return Mutes.MUTES.USERID;
    }

    @Override
    public Field<Long> field3() {
        return Mutes.MUTES.GUILDID;
    }

    @Override
    public Field<LocalDateTime> field4() {
        return Mutes.MUTES.MUTEDUNTIL;
    }

    @Override
    public Long component1() {
        return getMuteid();
    }

    @Override
    public Long component2() {
        return getUserid();
    }

    @Override
    public Long component3() {
        return getGuildid();
    }

    @Override
    public LocalDateTime component4() {
        return getMuteduntil();
    }

    @Override
    public Long value1() {
        return getMuteid();
    }

    @Override
    public Long value2() {
        return getUserid();
    }

    @Override
    public Long value3() {
        return getGuildid();
    }

    @Override
    public LocalDateTime value4() {
        return getMuteduntil();
    }

    @Override
    public MutesRecord value1(Long value) {
        setMuteid(value);
        return this;
    }

    @Override
    public MutesRecord value2(Long value) {
        setUserid(value);
        return this;
    }

    @Override
    public MutesRecord value3(Long value) {
        setGuildid(value);
        return this;
    }

    @Override
    public MutesRecord value4(LocalDateTime value) {
        setMuteduntil(value);
        return this;
    }

    @Override
    public MutesRecord values(Long value1, Long value2, Long value3, LocalDateTime value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MutesRecord
     */
    public MutesRecord() {
        super(Mutes.MUTES);
    }

    /**
     * Create a detached, initialised MutesRecord
     */
    public MutesRecord(Long muteid, Long userid, Long guildid, LocalDateTime muteduntil) {
        super(Mutes.MUTES);

        setMuteid(muteid);
        setUserid(userid);
        setGuildid(guildid);
        setMuteduntil(muteduntil);
    }
}
