package org.igsq.igsqbot.entities;

public enum ConfigOption
{
	TOKEN("token", "token", Filename.BOT),
	PRIVILEGEDUSERS("privilegedusers", "000000000000000000, 000000000000000001, 000000000000000002", Filename.BOT),

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

	DEFAULT("default", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	RISING("rising", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	FLYING("flying", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	SOARING("soaring", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	EPIC("epic", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	EPIC2("epic2", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	EPIC3("epic3", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	ELITE("elite", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	ELITE2("elite2", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	ELITE3("elite3", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	MOD("mod", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	MOD2("mod2", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	MOD3("mod3", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT),
	COUNCIL("council", "000000000000000000, 000000000000000001, 000000000000000002", Filename.MINECRAFT);



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
}
