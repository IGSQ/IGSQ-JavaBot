package org.igsq.igsqbot.entities;

import java.util.List;

public enum ConfigOption
{
	TOKEN("token", "token"),
	PRIVILEGEDUSERS("privilegedusers", "0000000000000"),
	HOMESERVER("homeserver", "000000000000000000"),

	LOCALUSERNAME("localusername", "username"),
	LOCALPASSWORD("localpassword", "password"),
	LOCALDRIVER("localdriver", "org.postgresql.Driver"),
	LOCALURL("localurl", "jdbc:type://host:port/database"),

	REMOTEUSERNAME("remoteusername", "username"),
	REMOTEPASSWORD("remotepassword", "password"),
	REMOTEDRIVER("remotedriver", "org.postgresql.Driver"),
	REMOTEURL("remoteurl", "jdbc:type://host:port/database"),

	DEFAULT("default", "0000000000000"),
	RISING("rising", "0000000000000"),
	FLYING("flying", "0000000000000"),
	SOARING("soaring", "0000000000000"),
	EPIC("epic", "0000000000000"),
	EPIC2("epic2", "0000000000000"),
	EPIC3("epic3", "0000000000000"),
	ELITE("elite", "0000000000000"),
	ELITE2("elite2", "0000000000000"),
	ELITE3("elite3", "0000000000000"),
	CELESTIAL("celestial", "0000000000000"),
	MOD("mod", "0000000000000"),
	MOD2("mod2", "0000000000000"),
	MOD3("mod3", "0000000000000"),
	COUNCIL("council", "0000000000000"),

	FOUNDER("founder", "0000000000000"),
	BIRTHDAY("birthday", "0000000000000"),
	NITROBOOST("nitroboost", "0000000000000"),
	SUPPORTER("supporter", "0000000000000"),
	DEVELOPER("developer", "0000000000000");

	private final String key;
	private final String defaultValue;

	ConfigOption(String key, String defaultValue)
	{
		this.key = key;
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue()
	{
		return defaultValue;
	}

	public String getKey()
	{
		return key;
	}

	public static List<ConfigOption> getRanks()
	{
		return List.of(DEFAULT, RISING, FLYING, SOARING, EPIC, EPIC2, EPIC3,
				ELITE, ELITE2, ELITE3, CELESTIAL, MOD, MOD2, MOD3, COUNCIL);
	}
}
