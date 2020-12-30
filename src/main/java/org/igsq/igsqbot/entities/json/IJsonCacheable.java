package org.igsq.igsqbot.entities.json;

public interface IJsonCacheable
{
	void set(IJson json);
	void remove(String primaryKey);
	void remove(IJson json);
	void load();
	void save();
}
