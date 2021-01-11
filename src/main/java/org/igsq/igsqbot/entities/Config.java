package org.igsq.igsqbot.entities;

import org.igsq.igsqbot.IGSQBot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class Config
{
	public static final String FOLDER = "config";
	private final IGSQBot igsqBot;
	private Properties botProperties;
	private Properties minecraftProperties;

	public Config(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		createFiles();
		loadFiles();
		applyDefaults();
	}

	private void createFiles()
	{
		File folder = new File(FOLDER);
		if(!folder.exists())
		{
			if(!folder.mkdir())
			{
				igsqBot.getLogger().error("Something went wrong when making the config folder");
			}
		}
		try
		{
			for(Filename filename : Filename.values())
			{
				new File(folder, filename.getName() + ".properties").createNewFile();
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An error occurred while creating the config files", exception);
		}
	}

	private void applyDefaults()
	{
		for(ConfigOption configOption : ConfigOption.values())
		{
			if(configOption.getFilename() == Filename.BOT && !botProperties.containsKey(configOption.getKey()))
			{
				botProperties.setProperty(configOption.getKey(), configOption.getDefaultValue());
			}
			else if(configOption.getFilename() == Filename.MINECRAFT && !minecraftProperties.containsKey(configOption.getKey()))
			{
				minecraftProperties.setProperty(configOption.getKey(), configOption.getDefaultValue());
			}
		}
		writeFile(botProperties, Filename.BOT);
		writeFile(minecraftProperties, Filename.MINECRAFT);
	}

	private void writeFile(Properties properties, Filename filename)
	{
		try
		{
			FileWriter fileWriter = new FileWriter(FOLDER + "/" + filename.getName() + ".properties");
			properties.store(fileWriter, "Created by IGSQBot");
			fileWriter.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("Something went wrong when saving the config", exception);
		}

	}

	private void loadFiles()
	{
		this.botProperties = new Properties();
		this.minecraftProperties = new Properties();
		try
		{
			FileReader botReader = new FileReader(FOLDER + "/" + Filename.BOT.getName() + ".properties");
			botProperties.load(botReader);
			botReader.close();

			FileReader minecraftReader = new FileReader(FOLDER + "/" + Filename.MINECRAFT.getName() + ".properties");
			minecraftProperties.load(minecraftReader);
			minecraftReader.close();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("An error occurred while loading the config files.");
		}
	}

	public String getOption(ConfigOption configOption)
	{
		if(configOption.getFilename() == Filename.BOT)
		{
			return botProperties.getProperty(configOption.getKey(), "-1");
		}
		else if(configOption.getFilename() == Filename.MINECRAFT)
		{
			return minecraftProperties.getProperty(configOption.getKey(), "-1");
		}
		else
		{
			return null;
		}
	}
}
