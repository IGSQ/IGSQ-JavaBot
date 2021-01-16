/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import java.time.LocalDateTime;
import org.igsq.igsqbot.entities.jooq.tables.Reports;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class ReportsRecord extends UpdatableRecordImpl<ReportsRecord>
        implements Record8<Long, Long, Long, Long, Long, Long, LocalDateTime, String>
{

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached ReportsRecord
     */
    public ReportsRecord()
    {
        super(Reports.REPORTS);
    }

    /**
     * Create a detached, initialised ReportsRecord
     */
    public ReportsRecord(Long id, Long messageid, Long reportmessageid, Long channelid, Long guildid, Long userid, LocalDateTime timestamp, String reporttext)
    {
        super(Reports.REPORTS);

        setId(id);
        setMessageid(messageid);
        setReportmessageid(reportmessageid);
        setChannelid(channelid);
        setGuildid(guildid);
        setUserid(userid);
        setTimestamp(timestamp);
        setReporttext(reporttext);
    }

    /**
     * Getter for <code>public.reports.id</code>.
     */
    public Long getId()
    {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.reports.id</code>.
     */
    public ReportsRecord setId(Long value)
    {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.reports.messageid</code>.
     */
    public Long getMessageid()
    {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.reports.messageid</code>.
     */
    public ReportsRecord setMessageid(Long value)
    {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.reports.reportmessageid</code>.
     */
    public Long getReportmessageid()
    {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.reports.reportmessageid</code>.
     */
    public ReportsRecord setReportmessageid(Long value)
    {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.reports.channelid</code>.
     */
    public Long getChannelid()
    {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.reports.channelid</code>.
     */
    public ReportsRecord setChannelid(Long value)
    {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.reports.guildid</code>.
     */
    public Long getGuildid()
    {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.reports.guildid</code>.
     */
    public ReportsRecord setGuildid(Long value)
    {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.reports.userid</code>.
     */
    public Long getUserid()
    {
        return (Long) get(5);
    }

    /**
     * Setter for <code>public.reports.userid</code>.
     */
    public ReportsRecord setUserid(Long value)
    {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.reports.timestamp</code>.
     */
    public LocalDateTime getTimestamp()
    {
        return (LocalDateTime) get(6);
    }

    /**
     * Setter for <code>public.reports.timestamp</code>.
     */
    public ReportsRecord setTimestamp(LocalDateTime value)
    {
        set(6, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.reports.reporttext</code>.
     */
    public String getReporttext()
    {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.reports.reporttext</code>.
     */
    public ReportsRecord setReporttext(String value)
    {
        set(7, value);
        return this;
    }

    @Override
    public Record1<Long> key()
    {
        return (Record1) super.key();
    }

    @Override
    public Row8<Long, Long, Long, Long, Long, Long, LocalDateTime, String> fieldsRow()
    {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<Long, Long, Long, Long, Long, Long, LocalDateTime, String> valuesRow()
    {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<Long> field1()
    {
        return Reports.REPORTS.ID;
    }

    @Override
    public Field<Long> field2()
    {
        return Reports.REPORTS.MESSAGEID;
    }

    @Override
    public Field<Long> field3()
    {
        return Reports.REPORTS.REPORTMESSAGEID;
    }

    @Override
    public Field<Long> field4()
    {
        return Reports.REPORTS.CHANNELID;
    }

    @Override
    public Field<Long> field5()
    {
        return Reports.REPORTS.GUILDID;
    }

    @Override
    public Field<Long> field6()
    {
        return Reports.REPORTS.USERID;
    }

    @Override
    public Field<LocalDateTime> field7()
    {
        return Reports.REPORTS.TIMESTAMP;
    }

    @Override
    public Field<String> field8()
    {
        return Reports.REPORTS.REPORTTEXT;
    }

    @Override
    public Long component1()
    {
        return getId();
    }

    @Override
    public Long component2()
    {
        return getMessageid();
    }

    @Override
    public Long component3()
    {
        return getReportmessageid();
    }

    @Override
    public Long component4()
    {
        return getChannelid();
    }

    @Override
    public Long component5()
    {
        return getGuildid();
    }

    @Override
    public Long component6()
    {
        return getUserid();
    }

    @Override
    public LocalDateTime component7()
    {
        return getTimestamp();
    }

    @Override
    public String component8()
    {
        return getReporttext();
    }

    @Override
    public Long value1()
    {
        return getId();
    }

    @Override
    public Long value2()
    {
        return getMessageid();
    }

    @Override
    public Long value3()
    {
        return getReportmessageid();
    }

    @Override
    public Long value4()
    {
        return getChannelid();
    }

    @Override
    public Long value5()
    {
        return getGuildid();
    }

    @Override
    public Long value6()
    {
        return getUserid();
    }

    @Override
    public LocalDateTime value7()
    {
        return getTimestamp();
    }

    @Override
    public String value8()
    {
        return getReporttext();
    }

    @Override
    public ReportsRecord value1(Long value)
    {
        setId(value);
        return this;
    }

    @Override
    public ReportsRecord value2(Long value)
    {
        setMessageid(value);
        return this;
    }

    @Override
    public ReportsRecord value3(Long value)
    {
        setReportmessageid(value);
        return this;
    }

    @Override
    public ReportsRecord value4(Long value)
    {
        setChannelid(value);
        return this;
    }

    @Override
    public ReportsRecord value5(Long value)
    {
        setGuildid(value);
        return this;
    }

    @Override
    public ReportsRecord value6(Long value)
    {
        setUserid(value);
        return this;
    }

    @Override
    public ReportsRecord value7(LocalDateTime value)
    {
        setTimestamp(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public ReportsRecord value8(String value)
    {
        setReporttext(value);
        return this;
    }

    @Override
    public ReportsRecord values(Long value1, Long value2, Long value3, Long value4, Long value5, Long value6, LocalDateTime value7, String value8)
    {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }
}
