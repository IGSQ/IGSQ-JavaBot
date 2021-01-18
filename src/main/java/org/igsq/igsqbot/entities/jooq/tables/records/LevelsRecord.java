/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import org.igsq.igsqbot.entities.jooq.tables.Levels;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.generated.tables.Levels;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LevelsRecord extends UpdatableRecordImpl<LevelsRecord> implements Record4<Long, Long, Long, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.levels.id</code>.
     */
    public LevelsRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.levels.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.levels.guild_id</code>.
     */
    public LevelsRecord setGuildId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.levels.guild_id</code>.
     */
    public Long getGuildId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.levels.role_id</code>.
     */
    public LevelsRecord setRoleId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.levels.role_id</code>.
     */
    public Long getRoleId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.levels.awarded_at</code>.
     */
    public LevelsRecord setAwardedAt(Integer value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.levels.awarded_at</code>.
     */
    public Integer getAwardedAt() {
        return (Integer) get(3);
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
    public Row4<Long, Long, Long, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Long, Long, Long, Integer> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Levels.LEVELS.ID;
    }

    @Override
    public Field<Long> field2() {
        return Levels.LEVELS.GUILD_ID;
    }

    @Override
    public Field<Long> field3() {
        return Levels.LEVELS.ROLE_ID;
    }

    @Override
    public Field<Integer> field4() {
        return Levels.LEVELS.AWARDED_AT;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getGuildId();
    }

    @Override
    public Long component3() {
        return getRoleId();
    }

    @Override
    public Integer component4() {
        return getAwardedAt();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getGuildId();
    }

    @Override
    public Long value3() {
        return getRoleId();
    }

    @Override
    public Integer value4() {
        return getAwardedAt();
    }

    @Override
    public LevelsRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public LevelsRecord value2(Long value) {
        setGuildId(value);
        return this;
    }

    @Override
    public LevelsRecord value3(Long value) {
        setRoleId(value);
        return this;
    }

    @Override
    public LevelsRecord value4(Integer value) {
        setAwardedAt(value);
        return this;
    }

    @Override
    public LevelsRecord values(Long value1, Long value2, Long value3, Integer value4) {
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
     * Create a detached LevelsRecord
     */
    public LevelsRecord() {
        super(Levels.LEVELS);
    }

    /**
     * Create a detached, initialised LevelsRecord
     */
    public LevelsRecord(Long id, Long guildId, Long roleId, Integer awardedAt) {
        super(Levels.LEVELS);

        setId(id);
        setGuildId(guildId);
        setRoleId(roleId);
        setAwardedAt(awardedAt);
    }
}
