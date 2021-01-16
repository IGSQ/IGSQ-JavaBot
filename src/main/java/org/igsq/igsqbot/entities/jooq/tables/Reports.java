/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.igsq.igsqbot.entities.jooq.Keys;
import org.igsq.igsqbot.entities.jooq.Public;
import org.igsq.igsqbot.entities.jooq.tables.records.ReportsRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Reports extends TableImpl<ReportsRecord>
{

	/**
	 * The reference instance of <code>public.reports</code>
	 */
	public static final Reports REPORTS = new Reports();
	private static final long serialVersionUID = 1L;
	/**
	 * The column <code>public.reports.id</code>.
	 */
	public final TableField<ReportsRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");
	/**
	 * The column <code>public.reports.messageid</code>.
	 */
	public final TableField<ReportsRecord, Long> MESSAGEID = createField(DSL.name("messageid"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.reports.reportmessageid</code>.
	 */
	public final TableField<ReportsRecord, Long> REPORTMESSAGEID = createField(DSL.name("reportmessageid"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.reports.channelid</code>.
	 */
	public final TableField<ReportsRecord, Long> CHANNELID = createField(DSL.name("channelid"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.reports.guildid</code>.
	 */
	public final TableField<ReportsRecord, Long> GUILDID = createField(DSL.name("guildid"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.reports.userid</code>.
	 */
	public final TableField<ReportsRecord, Long> USERID = createField(DSL.name("userid"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.reports.timestamp</code>.
	 */
	public final TableField<ReportsRecord, LocalDateTime> TIMESTAMP = createField(DSL.name("timestamp"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");
	/**
	 * The column <code>public.reports.reporttext</code>.
	 */
	public final TableField<ReportsRecord, String> REPORTTEXT = createField(DSL.name("reporttext"), SQLDataType.CLOB.nullable(false), this, "");

	private Reports(Name alias, Table<ReportsRecord> aliased)
	{
		this(alias, aliased, null);
	}

	private Reports(Name alias, Table<ReportsRecord> aliased, Field<?>[] parameters)
	{
		super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
	}

	/**
	 * Create an aliased <code>public.reports</code> table reference
	 */
	public Reports(String alias)
	{
		this(DSL.name(alias), REPORTS);
	}

	/**
	 * Create an aliased <code>public.reports</code> table reference
	 */
	public Reports(Name alias)
	{
		this(alias, REPORTS);
	}

	/**
	 * Create a <code>public.reports</code> table reference
	 */
	public Reports()
	{
		this(DSL.name("reports"), null);
	}

	public <O extends Record> Reports(Table<O> child, ForeignKey<O, ReportsRecord> key)
	{
		super(child, key, REPORTS);
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ReportsRecord> getRecordType()
	{
		return ReportsRecord.class;
	}

	@Override
	public Schema getSchema()
	{
		return Public.PUBLIC;
	}

	@Override
	public Identity<ReportsRecord, Long> getIdentity()
	{
		return (Identity<ReportsRecord, Long>) super.getIdentity();
	}

	@Override
	public UniqueKey<ReportsRecord> getPrimaryKey()
	{
		return Keys.REPORTS_PKEY;
	}

	@Override
	public List<UniqueKey<ReportsRecord>> getKeys()
	{
		return Arrays.<UniqueKey<ReportsRecord>>asList(Keys.REPORTS_PKEY, Keys.REPORTS_MESSAGEID_KEY, Keys.REPORTS_REPORTMESSAGEID_KEY);
	}

	@Override
	public List<ForeignKey<ReportsRecord, ?>> getReferences()
	{
		return Arrays.<ForeignKey<ReportsRecord, ?>>asList(Keys.REPORTS__REPORTS_GUILDID_FKEY);
	}

	public Guilds guilds()
	{
		return new Guilds(this, Keys.REPORTS__REPORTS_GUILDID_FKEY);
	}

	@Override
	public Reports as(String alias)
	{
		return new Reports(DSL.name(alias), this);
	}

	@Override
	public Reports as(Name alias)
	{
		return new Reports(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Reports rename(String name)
	{
		return new Reports(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Reports rename(Name name)
	{
		return new Reports(name, null);
	}

	// -------------------------------------------------------------------------
	// Row8 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row8<Long, Long, Long, Long, Long, Long, LocalDateTime, String> fieldsRow()
	{
		return (Row8) super.fieldsRow();
	}
}
