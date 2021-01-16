/*
 * This file is generated by jOOQ.
 */
package org.igsq.igsqbot.entities.jooq.tables.records;


import org.igsq.igsqbot.entities.jooq.tables.ReactionRoles;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class ReactionRolesRecord extends UpdatableRecordImpl<ReactionRolesRecord>
		implements Record4<Long, Long, String, Long>
{

	private static final long serialVersionUID = 1L;

	/**
	 * Create a detached ReactionRolesRecord
	 */
	public ReactionRolesRecord()
	{
		super(ReactionRoles.REACTION_ROLES);
	}

	/**
	 * Create a detached, initialised ReactionRolesRecord
	 */
	public ReactionRolesRecord(Long id, Long guildid, String emoteid, Long roleid)
	{
		super(ReactionRoles.REACTION_ROLES);

		setId(id);
		setGuildid(guildid);
		setEmoteid(emoteid);
		setRoleid(roleid);
	}

	/**
	 * Getter for <code>public.reaction_roles.id</code>.
	 */
	public Long getId()
	{
		return (Long) get(0);
	}

	/**
	 * Setter for <code>public.reaction_roles.id</code>.
	 */
	public ReactionRolesRecord setId(Long value)
	{
		set(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.reaction_roles.guildid</code>.
	 */
	public Long getGuildid()
	{
		return (Long) get(1);
	}

	/**
	 * Setter for <code>public.reaction_roles.guildid</code>.
	 */
	public ReactionRolesRecord setGuildid(Long value)
	{
		set(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.reaction_roles.emoteid</code>.
	 */
	public String getEmoteid()
	{
		return (String) get(2);
	}

	/**
	 * Setter for <code>public.reaction_roles.emoteid</code>.
	 */
	public ReactionRolesRecord setEmoteid(String value)
	{
		set(2, value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * Getter for <code>public.reaction_roles.roleid</code>.
	 */
	public Long getRoleid()
	{
		return (Long) get(3);
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * Setter for <code>public.reaction_roles.roleid</code>.
	 */
	public ReactionRolesRecord setRoleid(Long value)
	{
		set(3, value);
		return this;
	}

	@Override
	public Record1<Long> key()
	{
		return (Record1) super.key();
	}

	@Override
	public Row4<Long, Long, String, Long> fieldsRow()
	{
		return (Row4) super.fieldsRow();
	}

	@Override
	public Row4<Long, Long, String, Long> valuesRow()
	{
		return (Row4) super.valuesRow();
	}

	@Override
	public Field<Long> field1()
	{
		return ReactionRoles.REACTION_ROLES.ID;
	}

	@Override
	public Field<Long> field2()
	{
		return ReactionRoles.REACTION_ROLES.GUILDID;
	}

	@Override
	public Field<String> field3()
	{
		return ReactionRoles.REACTION_ROLES.EMOTEID;
	}

	@Override
	public Field<Long> field4()
	{
		return ReactionRoles.REACTION_ROLES.ROLEID;
	}

	@Override
	public Long component1()
	{
		return getId();
	}

	@Override
	public Long component2()
	{
		return getGuildid();
	}

	@Override
	public String component3()
	{
		return getEmoteid();
	}

	@Override
	public Long component4()
	{
		return getRoleid();
	}

	@Override
	public Long value1()
	{
		return getId();
	}

	@Override
	public Long value2()
	{
		return getGuildid();
	}

	@Override
	public String value3()
	{
		return getEmoteid();
	}

	@Override
	public Long value4()
	{
		return getRoleid();
	}

	@Override
	public ReactionRolesRecord value1(Long value)
	{
		setId(value);
		return this;
	}

	@Override
	public ReactionRolesRecord value2(Long value)
	{
		setGuildid(value);
		return this;
	}

	@Override
	public ReactionRolesRecord value3(String value)
	{
		setEmoteid(value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	@Override
	public ReactionRolesRecord value4(Long value)
	{
		setRoleid(value);
		return this;
	}

	@Override
	public ReactionRolesRecord values(Long value1, Long value2, String value3, Long value4)
	{
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		return this;
	}
}
