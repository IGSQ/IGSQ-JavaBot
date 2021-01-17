package org.igsq.igsqbot.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.igsq.igsqbot.IGSQBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration
{
	public static final File CONFIG_FOLDER = new File("config");
	public static final File CONFIG_FILE = new File(CONFIG_FOLDER, "bot.cfg");
	private final IGSQBot igsqBot;
	private List<ConfigurationValue> configValues = new CopyOnWriteArrayList<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

	public Configuration(IGSQBot igsqBot)
	{
		this.igsqBot = igsqBot;
		initFolder();
		initFiles();
		loadFiles();
	}

	private void initFolder()
	{
		if(CONFIG_FOLDER.mkdir())
		{
			LOGGER.debug("Created new config folder.");
		}
		else
		{
			LOGGER.debug("Config folder exists.");
		}
	}

	private void initFiles()
	{
		try
		{
			if(CONFIG_FILE.createNewFile())
			{
				LOGGER.debug("Created new config file.");
			}
			else
			{
				LOGGER.debug("Config file exists.");
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("A config error occurred", exception);
		}
	}

	private void loadFiles()
	{
		List<ConfigurationValue> values = new ArrayList<>();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
			String line;
			while((line = reader.readLine()) != null)
			{
				if(!line.contains("=") || line.startsWith("#"))
				{
					continue;
				}
				String[] elements = line.split("=");
				values.add(new ConfigurationValue(elements[0], elements[1]));
			}
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("A config error occurred", exception);
		}
		applyDefaults(values);
	}

	private void applyDefaults(List<ConfigurationValue> loadedValues)
	{
		for(ConfigOption configOption : ConfigOption.values())
		{
			if(loadedValues.stream().map(ConfigurationValue::getKey).noneMatch(key -> configOption.getKey().equals(key)))
			{
				loadedValues.add(new ConfigurationValue(configOption.getKey(), configOption.getDefaultValue()));
			}
		}
		this.configValues = Collections.unmodifiableList(loadedValues);
		save();
	}

	private void save()
	{
		StringBuilder stringBuilder = new StringBuilder();
		for(ConfigurationValue configurationValue : configValues)
		{
			stringBuilder
					.append(configurationValue.getKey())
					.append("=")
					.append(configurationValue.getValue())
					.append("\n");
		}
		try
		{
			FileWriter fileWriter = new FileWriter(CONFIG_FILE);
			fileWriter.write(stringBuilder.toString());
			fileWriter.flush();
		}
		catch(Exception exception)
		{
			igsqBot.getLogger().error("A config error occurred", exception);
		}
	}

	public String getString(ConfigOption configOption)
	{
		for(ConfigurationValue configurationValue : configValues)
		{
			if(configurationValue.getKey().equals(configOption.getKey()))
			{
				return configurationValue.getValue();
			}
		}

		return configOption.getDefaultValue();
	}

	private static class ConfigurationValue
	{
		private String key;
		private String value;

		public ConfigurationValue(String key, String value)
		{
			this.key = key;
			this.value = value;
		}

		public String getKey()
		{
			return key;
		}

		public void setKey(String key)
		{
			this.key = key;
		}

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}
}
