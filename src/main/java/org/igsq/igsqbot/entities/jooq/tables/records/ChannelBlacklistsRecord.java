/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.igsq.igsqbot.entities.jooq.tables.ChannelBlacklists;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ChannelBlacklistsRecord extends UpdatableRecordImpl<ChannelBlacklistsRecord> implements Record3<Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.channel_blacklists.id</code>.
     */
    public ChannelBlacklistsRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.channel_blacklists.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.channel_blacklists.guild_id</code>.
     */
    public ChannelBlacklistsRecord setGuildId(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.channel_blacklists.guild_id</code>.
     */
    public Long getGuildId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.channel_blacklists.channel_id</code>.
     */
    public ChannelBlacklistsRecord setChannelId(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.channel_blacklists.channel_id</code>.
     */
    public Long getChannelId() {
        return (Long) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, Long, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Long, Long, Long> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return ChannelBlacklists.CHANNEL_BLACKLISTS.ID;
    }

    @Override
    public Field<Long> field2() {
        return ChannelBlacklists.CHANNEL_BLACKLISTS.GUILD_ID;
    }

    @Override
    public Field<Long> field3() {
        return ChannelBlacklists.CHANNEL_BLACKLISTS.CHANNEL_ID;
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
        return getChannelId();
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
        return getChannelId();
    }

    @Override
    public ChannelBlacklistsRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ChannelBlacklistsRecord value2(Long value) {
        setGuildId(value);
        return this;
    }

    @Override
    public ChannelBlacklistsRecord value3(Long value) {
        setChannelId(value);
        return this;
    }

    @Override
    public ChannelBlacklistsRecord values(Long value1, Long value2, Long value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChannelBlacklistsRecord
     */
    public ChannelBlacklistsRecord() {
        super(ChannelBlacklists.CHANNEL_BLACKLISTS);
    }

    /**
     * Create a detached, initialised ChannelBlacklistsRecord
     */
    public ChannelBlacklistsRecord(Long id, Long guildId, Long channelId) {
        super(ChannelBlacklists.CHANNEL_BLACKLISTS);

        setId(id);
        setGuildId(guildId);
        setChannelId(channelId);
    }
}
