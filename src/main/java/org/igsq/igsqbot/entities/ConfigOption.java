package org.igsq.igsqbot.entities;

import java.util.List;

public enum ConfigOption
{
	TOKEN("token", "token", Filename.BOT),
	PRIVILEGEDUSERS("privilegedusers", "0000000000000", Filename.BOT),
	HOMESERVER("homeserver", "000000000000000000", Filename.BOT),

	LOCALUSERNAME("localusername", "username", Filename.BOT),
	LOCALPASSWORD("localpassword", "password", Filename.BOT),
	LOCALTYPE("localtype", "postgresql", Filename.BOT),
	LOCALADDRESS("localaddress", "localhost", Filename.BOT),
	LOCALPORT("localport", "5432", Filename.BOT),
	LOCALDATABASE("localdatabase", "igsqbot", Filename.BOT),
	LOCALDRIVER("localdriver", "org.postgresql.Driver", Filename.BOT),

	REMOTEUSERNAME("remoteusername", "username", Filename.BOT),
	REMOTEPASSWORD("remotepassword", "password", Filename.BOT),
	REMOTETYPE("remotetype", "postgresql", Filename.BOT),
	REMOTEADDRESS("remoteaddress", "0.0.0.0", Filename.BOT),
	REMOTEPORT("remoteport", "5432", Filename.BOT),
	REMOTEDATABASE("remotedatabase", "igsqbot", Filename.BOT),
	REMOTEDRIVER("remotedriver", "org.postgresql.Driver", Filename.BOT),

	DEFAULT("default", "0000000000000", Filename.MINECRAFT),
	RISING("rising", "0000000000000", Filename.MINECRAFT),
	FLYING("flying", "0000000000000", Filename.MINECRAFT),
	SOARING("soaring", "0000000000000", Filename.MINECRAFT),
	EPIC("epic", "0000000000000", Filename.MINECRAFT),
	EPIC2("epic2", "0000000000000", Filename.MINECRAFT),
	EPIC3("epic3", "0000000000000", Filename.MINECRAFT),
	ELITE("elite", "0000000000000", Filename.MINECRAFT),
	ELITE2("elite2", "0000000000000", Filename.MINECRAFT),
	ELITE3("elite3", "0000000000000", Filename.MINECRAFT),
	MOD("mod", "0000000000000", Filename.MINECRAFT),
	MOD2("mod2", "0000000000000", Filename.MINECRAFT),
	MOD3("mod3", "0000000000000", Filename.MINECRAFT),
	COUNCIL("council", "0000000000000", Filename.MINECRAFT),

	FOUNDER("founder", "0000000000000", Filename.MINECRAFT),
	BIRTHDAY("birthday", "0000000000000", Filename.MINECRAFT),
	NITROBOOST("nitroboost", "0000000000000", Filename.MINECRAFT),
	SUPPORTER("supporter", "0000000000000", Filename.MINECRAFT),
	DEVELOPER("developer", "0000000000000", Filename.MINECRAFT);

	private final String key;
	private final String defaultValue;
	private final Filename filename;

	ConfigOption(String key, String defaultValue, Filename filename)
	{
		this.key = key;
		this.defaultValue = defaultValue;
		this.filename = filename;
	}

	public Filename getFilename()
	{
		return filename;
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
				ELITE, ELITE2, ELITE3, MOD, MOD2, MOD3, COUNCIL);
	}
}
