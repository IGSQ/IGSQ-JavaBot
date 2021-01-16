/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.igsq.igsqbot.entities.jooq.Keys;
import org.igsq.igsqbot.entities.jooq.Public;
import org.igsq.igsqbot.entities.jooq.tables.records.WarningsRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Warnings extends TableImpl<WarningsRecord>
{

	/**
	 * The reference instance of <code>public.warnings</code>
	 */
	public static final Warnings WARNINGS = new Warnings();
	private static final long serialVersionUID = 1L;
	/**
	 * The column <code>public.warnings.warnid</code>.
	 */
	public final TableField<WarningsRecord, Long> WARNID = createField(DSL.name("warnid"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");
	/**
	 * The column <code>public.warnings.guildid</code>.
	 */
	public final TableField<WarningsRecord, Long> GUILDID = createField(DSL.name("guildid"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.warnings.userid</code>.
	 */
	public final TableField<WarningsRecord, Long> USERID = createField(DSL.name("userid"), SQLDataType.BIGINT.nullable(false), this, "");
	/**
	 * The column <code>public.warnings.timestamp</code>.
	 */
	public final TableField<WarningsRecord, LocalDateTime> TIMESTAMP = createField(DSL.name("timestamp"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field("CURRENT_TIMESTAMP", SQLDataType.LOCALDATETIME)), this, "");
	/**
	 * The column <code>public.warnings.warntext</code>.
	 */
	public final TableField<WarningsRecord, String> WARNTEXT = createField(DSL.name("warntext"), SQLDataType.CLOB.nullable(false), this, "");

	private Warnings(Name alias, Table<WarningsRecord> aliased)
	{
		this(alias, aliased, null);
	}

	private Warnings(Name alias, Table<WarningsRecord> aliased, Field<?>[] parameters)
	{
		super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
	}

	/**
	 * Create an aliased <code>public.warnings</code> table reference
	 */
	public Warnings(String alias)
	{
		this(DSL.name(alias), WARNINGS);
	}

	/**
	 * Create an aliased <code>public.warnings</code> table reference
	 */
	public Warnings(Name alias)
	{
		this(alias, WARNINGS);
	}

	/**
	 * Create a <code>public.warnings</code> table reference
	 */
	public Warnings()
	{
		this(DSL.name("warnings"), null);
	}

	public <O extends Record> Warnings(Table<O> child, ForeignKey<O, WarningsRecord> key)
	{
		super(child, key, WARNINGS);
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<WarningsRecord> getRecordType()
	{
		return WarningsRecord.class;
	}

	@Override
	public Schema getSchema()
	{
		return Public.PUBLIC;
	}

	@Override
	public Identity<WarningsRecord, Long> getIdentity()
	{
		return (Identity<WarningsRecord, Long>) super.getIdentity();
	}

	@Override
	public UniqueKey<WarningsRecord> getPrimaryKey()
	{
		return Keys.WARNINGS_PKEY;
	}

	@Override
	public List<UniqueKey<WarningsRecord>> getKeys()
	{
		return Arrays.<UniqueKey<WarningsRecord>>asList(Keys.WARNINGS_PKEY);
	}

	@Override
	public List<ForeignKey<WarningsRecord, ?>> getReferences()
	{
		return Arrays.<ForeignKey<WarningsRecord, ?>>asList(Keys.WARNINGS__WARNINGS_GUILDID_FKEY);
	}

	public Guilds guilds()
	{
		return new Guilds(this, Keys.WARNINGS__WARNINGS_GUILDID_FKEY);
	}

	@Override
	public Warnings as(String alias)
	{
		return new Warnings(DSL.name(alias), this);
	}

	@Override
	public Warnings as(Name alias)
	{
		return new Warnings(alias, this);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Warnings rename(String name)
	{
		return new Warnings(DSL.name(name), null);
	}

	/**
	 * Rename this table
	 */
	@Override
	public Warnings rename(Name name)
	{
		return new Warnings(name, null);
	}

	// -------------------------------------------------------------------------
	// Row5 type methods
	// -------------------------------------------------------------------------

	@Override
	public Row5<Long, Long, Long, LocalDateTime, String> fieldsRow()
	{
		return (Row5) super.fieldsRow();
	}
}
