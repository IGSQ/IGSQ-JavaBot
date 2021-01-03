package org.igsq.igsqbot.entities.json;

public interface IJsonCacheable
{
	void set(IJsonEntity json);

	void remove(String primaryKey);

	void remove(IJsonEntity json);

	void load();

	void save();

	void reload();
}
