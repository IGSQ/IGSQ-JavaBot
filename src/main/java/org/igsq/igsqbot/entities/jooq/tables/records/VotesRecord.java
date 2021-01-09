/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import java.time.LocalDateTime;

import org.igsq.igsqbot.entities.jooq.tables.Votes;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VotesRecord extends UpdatableRecordImpl<VotesRecord> implements Record5<Long, Long, Long, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.votes.id</code>.
     */
    public VotesRecord setId(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.votes.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.votes.voteid</code>.
     */
    public VotesRecord setVoteid(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.votes.voteid</code>.
     */
    public Long getVoteid() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.votes.messageid</code>.
     */
    public VotesRecord setMessageid(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.votes.messageid</code>.
     */
    public Long getMessageid() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.votes.option</code>.
     */
    public VotesRecord setOption(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.votes.option</code>.
     */
    public String getOption() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.votes.timestamp</code>.
     */
    public VotesRecord setTimestamp(LocalDateTime value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.votes.timestamp</code>.
     */
    public LocalDateTime getTimestamp() {
        return (LocalDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, Long, Long, String, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, Long, Long, String, LocalDateTime> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Votes.VOTES.ID;
    }

    @Override
    public Field<Long> field2() {
        return Votes.VOTES.VOTEID;
    }

    @Override
    public Field<Long> field3() {
        return Votes.VOTES.MESSAGEID;
    }

    @Override
    public Field<String> field4() {
        return Votes.VOTES.OPTION;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return Votes.VOTES.TIMESTAMP;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Long component2() {
        return getVoteid();
    }

    @Override
    public Long component3() {
        return getMessageid();
    }

    @Override
    public String component4() {
        return getOption();
    }

    @Override
    public LocalDateTime component5() {
        return getTimestamp();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Long value2() {
        return getVoteid();
    }

    @Override
    public Long value3() {
        return getMessageid();
    }

    @Override
    public String value4() {
        return getOption();
    }

    @Override
    public LocalDateTime value5() {
        return getTimestamp();
    }

    @Override
    public VotesRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public VotesRecord value2(Long value) {
        setVoteid(value);
        return this;
    }

    @Override
    public VotesRecord value3(Long value) {
        setMessageid(value);
        return this;
    }

    @Override
    public VotesRecord value4(String value) {
        setOption(value);
        return this;
    }

    @Override
    public VotesRecord value5(LocalDateTime value) {
        setTimestamp(value);
        return this;
    }

    @Override
    public VotesRecord values(Long value1, Long value2, Long value3, String value4, LocalDateTime value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached VotesRecord
     */
    public VotesRecord() {
        super(Votes.VOTES);
    }

    /**
     * Create a detached, initialised VotesRecord
     */
    public VotesRecord(Long id, Long voteid, Long messageid, String option, LocalDateTime timestamp) {
        super(Votes.VOTES);

        setId(id);
        setVoteid(voteid);
        setMessageid(messageid);
        setOption(option);
        setTimestamp(timestamp);
    }
}
