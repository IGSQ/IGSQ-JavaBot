/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import java.time.LocalDateTime;
import org.igsq.igsqbot.entities.jooq.tables.Warnings;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WarningsRecord extends UpdatableRecordImpl<WarningsRecord>
        implements Record5<Long, Long, Long, LocalDateTime, String>
{

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached WarningsRecord
     */
    public WarningsRecord()
    {
        super(Warnings.WARNINGS);
    }

    /**
     * Create a detached, initialised WarningsRecord
     */
    public WarningsRecord(Long warnid, Long guildid, Long userid, LocalDateTime timestamp, String warntext)
    {
        super(Warnings.WARNINGS);

        setWarnid(warnid);
        setGuildid(guildid);
        setUserid(userid);
        setTimestamp(timestamp);
        setWarntext(warntext);
    }

    /**
     * Getter for <code>public.warnings.warnid</code>.
     */
    public Long getWarnid()
    {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.warnings.warnid</code>.
     */
    public WarningsRecord setWarnid(Long value)
    {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.warnings.guildid</code>.
     */
    public Long getGuildid()
    {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.warnings.guildid</code>.
     */
    public WarningsRecord setGuildid(Long value)
    {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.warnings.userid</code>.
     */
    public Long getUserid()
    {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.warnings.userid</code>.
     */
    public WarningsRecord setUserid(Long value)
    {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.warnings.timestamp</code>.
     */
    public LocalDateTime getTimestamp()
    {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>public.warnings.timestamp</code>.
     */
    public WarningsRecord setTimestamp(LocalDateTime value)
    {
        set(3, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.warnings.warntext</code>.
     */
    public String getWarntext()
    {
        return (String) get(4);
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.warnings.warntext</code>.
     */
    public WarningsRecord setWarntext(String value)
    {
        set(4, value);
        return this;
    }

    @Override
    public Record1<Long> key()
    {
        return (Record1) super.key();
    }

    @Override
    public Row5<Long, Long, Long, LocalDateTime, String> fieldsRow()
    {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, Long, Long, LocalDateTime, String> valuesRow()
    {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1()
    {
        return Warnings.WARNINGS.WARNID;
    }

    @Override
    public Field<Long> field2()
    {
        return Warnings.WARNINGS.GUILDID;
    }

    @Override
    public Field<Long> field3()
    {
        return Warnings.WARNINGS.USERID;
    }

    @Override
    public Field<LocalDateTime> field4()
    {
        return Warnings.WARNINGS.TIMESTAMP;
    }

    @Override
    public Field<String> field5()
    {
        return Warnings.WARNINGS.WARNTEXT;
    }

    @Override
    public Long component1()
    {
        return getWarnid();
    }

    @Override
    public Long component2()
    {
        return getGuildid();
    }

    @Override
    public Long component3()
    {
        return getUserid();
    }

    @Override
    public LocalDateTime component4()
    {
        return getTimestamp();
    }

    @Override
    public String component5()
    {
        return getWarntext();
    }

    @Override
    public Long value1()
    {
        return getWarnid();
    }

    @Override
    public Long value2()
    {
        return getGuildid();
    }

    @Override
    public Long value3()
    {
        return getUserid();
    }

    @Override
    public LocalDateTime value4()
    {
        return getTimestamp();
    }

    @Override
    public String value5()
    {
        return getWarntext();
    }

    @Override
    public WarningsRecord value1(Long value)
    {
        setWarnid(value);
        return this;
    }

    @Override
    public WarningsRecord value2(Long value)
    {
        setGuildid(value);
        return this;
    }

    @Override
    public WarningsRecord value3(Long value)
    {
        setUserid(value);
        return this;
    }

    @Override
    public WarningsRecord value4(LocalDateTime value)
    {
        setTimestamp(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public WarningsRecord value5(String value)
    {
        setWarntext(value);
        return this;
    }

    @Override
    public WarningsRecord values(Long value1, Long value2, Long value3, LocalDateTime value4, String value5)
    {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }
}
