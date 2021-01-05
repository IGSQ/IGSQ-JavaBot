/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import org.igsq.igsqbot.entities.jooq.tables.Guilds;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GuildsRecord extends UpdatableRecordImpl<GuildsRecord> implements Record7<Long, Long, Long, Long, Long, Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.guilds.guildid</code>.
     */
    public GuildsRecord setGuildid(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.guildid</code>.
     */
    public Long getGuildid() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.guilds.logchannel</code>.
     */
    public GuildsRecord setLogchannel(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.logchannel</code>.
     */
    public Long getLogchannel() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.guilds.mutedrole</code>.
     */
    public GuildsRecord setMutedrole(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.mutedrole</code>.
     */
    public Long getMutedrole() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.guilds.verifiedrole</code>.
     */
    public GuildsRecord setVerifiedrole(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.verifiedrole</code>.
     */
    public Long getVerifiedrole() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.guilds.reportchannel</code>.
     */
    public GuildsRecord setReportchannel(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.reportchannel</code>.
     */
    public Long getReportchannel() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.guilds.votechannel</code>.
     */
    public GuildsRecord setVotechannel(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.votechannel</code>.
     */
    public Long getVotechannel() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>public.guilds.prefix</code>.
     */
    public GuildsRecord setPrefix(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.guilds.prefix</code>.
     */
    public String getPrefix() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, Long, Long, Long, Long, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<Long, Long, Long, Long, Long, Long, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Guilds.GUILDS.GUILDID;
    }

    @Override
    public Field<Long> field2() {
        return Guilds.GUILDS.LOGCHANNEL;
    }

    @Override
    public Field<Long> field3() {
        return Guilds.GUILDS.MUTEDROLE;
    }

    @Override
    public Field<Long> field4() {
        return Guilds.GUILDS.VERIFIEDROLE;
    }

    @Override
    public Field<Long> field5() {
        return Guilds.GUILDS.REPORTCHANNEL;
    }

    @Override
    public Field<Long> field6() {
        return Guilds.GUILDS.VOTECHANNEL;
    }

    @Override
    public Field<String> field7() {
        return Guilds.GUILDS.PREFIX;
    }

    @Override
    public Long component1() {
        return getGuildid();
    }

    @Override
    public Long component2() {
        return getLogchannel();
    }

    @Override
    public Long component3() {
        return getMutedrole();
    }

    @Override
    public Long component4() {
        return getVerifiedrole();
    }

    @Override
    public Long component5() {
        return getReportchannel();
    }

    @Override
    public Long component6() {
        return getVotechannel();
    }

    @Override
    public String component7() {
        return getPrefix();
    }

    @Override
    public Long value1() {
        return getGuildid();
    }

    @Override
    public Long value2() {
        return getLogchannel();
    }

    @Override
    public Long value3() {
        return getMutedrole();
    }

    @Override
    public Long value4() {
        return getVerifiedrole();
    }

    @Override
    public Long value5() {
        return getReportchannel();
    }

    @Override
    public Long value6() {
        return getVotechannel();
    }

    @Override
    public String value7() {
        return getPrefix();
    }

    @Override
    public GuildsRecord value1(Long value) {
        setGuildid(value);
        return this;
    }

    @Override
    public GuildsRecord value2(Long value) {
        setLogchannel(value);
        return this;
    }

    @Override
    public GuildsRecord value3(Long value) {
        setMutedrole(value);
        return this;
    }

    @Override
    public GuildsRecord value4(Long value) {
        setVerifiedrole(value);
        return this;
    }

    @Override
    public GuildsRecord value5(Long value) {
        setReportchannel(value);
        return this;
    }

    @Override
    public GuildsRecord value6(Long value) {
        setVotechannel(value);
        return this;
    }

    @Override
    public GuildsRecord value7(String value) {
        setPrefix(value);
        return this;
    }

    @Override
    public GuildsRecord values(Long value1, Long value2, Long value3, Long value4, Long value5, Long value6, String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached GuildsRecord
     */
    public GuildsRecord() {
        super(Guilds.GUILDS);
    }

    /**
     * Create a detached, initialised GuildsRecord
     */
    public GuildsRecord(Long guildid, Long logchannel, Long mutedrole, Long verifiedrole, Long reportchannel, Long votechannel, String prefix) {
        super(Guilds.GUILDS);

        setGuildid(guildid);
        setLogchannel(logchannel);
        setMutedrole(mutedrole);
        setVerifiedrole(verifiedrole);
        setReportchannel(reportchannel);
        setVotechannel(votechannel);
        setPrefix(prefix);
    }
}
