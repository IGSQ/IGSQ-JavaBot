package org.igsq.igsqbot.entities.yaml;

public enum Filename
{
	CONFIG("config"),
	INTERNAL("internal"),
	GUILD("guild"),
	VERIFICATION("verification"),
	MINECRAFT("minecraft"),
	PUNISHMENT("punishment"),
	ALL("all");

	private final String fileName;

	private Filename(String filename)
	{
		this.fileName = filename;
	}

	private String getFileName()
	{
		return fileName;
	}
}
