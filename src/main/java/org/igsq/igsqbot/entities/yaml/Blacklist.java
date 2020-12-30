package org.igsq.igsqbot.entities.yaml;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.util.ArrayUtils;
import org.igsq.igsqbot.util.YamlUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Blacklist
{
	private final UUID name;
	private final String delimiter;
	private static final Map<UUID, Blacklist> BLACKLIST_MAP;
	static
	{
		BLACKLIST_MAP = ExpiringMap.builder()
				.maxSize(1000)
				.expirationPolicy(ExpirationPolicy.ACCESSED)
				.expiration(30, TimeUnit.MINUTES)
				.build();
	}
	public Blacklist(String delimiter)
	{
		this.name = getName();
		this.delimiter = delimiter;
		BLACKLIST_MAP.putIfAbsent(name, this);
	}
	
	public Blacklist append(Object data)
	{
		if(isFirstEntry())
		{
			Yaml.updateField(name + ".blacklist", Filename.INTERNAL, data);
		}
		else
		{
			Yaml.updateField(name + ".blacklist", Filename.INTERNAL, data + delimiter);
		}
		return this;
	}

	public Blacklist depend(Object data)
	{
		List<String> onFile = get();
		onFile.remove(data);
		Yaml.updateField(name + ".blacklist", Filename.INTERNAL, ArrayUtils.arrayCompile(onFile, delimiter));
		return this;
	}

	public Blacklist depend(int position)
	{
		List<String> onFile = get();
		onFile.remove(position);
		Yaml.updateField(name + ".blacklist", Filename.INTERNAL, ArrayUtils.arrayCompile(onFile, delimiter));
		return this;
	}

	public List<String> get()
	{
		return Arrays.asList(Yaml.getFieldString(name + ".blacklist", Filename.INTERNAL).split(delimiter));
	}
	private boolean isFirstEntry()
	{
		return YamlUtils.isFieldEmpty(name + ".blacklist", Filename.INTERNAL);
	}

	private UUID getName()
	{
		UUID uuid = UUID.randomUUID();
		if(BLACKLIST_MAP.containsKey(uuid))
		{
			return getName();
		}
		else
		{
			return uuid;
		}
	}

	public static void close()
	{
		BLACKLIST_MAP.keySet().forEach(uuid -> YamlUtils.clearField(uuid + ".blacklist", Filename.INTERNAL));
	}
}
