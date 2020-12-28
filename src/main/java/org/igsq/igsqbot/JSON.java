package org.igsq.igsqbot;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.igsq.igsqbot.entities.yaml.Filename;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSON
{
	private JSON()
	{
		//Overrides the default, public, constructor
	}
	private static final Map<Filename, File> FILES = new ConcurrentHashMap<>(); //Static reference to all the available files, linked to a filename
	private static final Map<Filename, DocumentContext> JSON_CACHE = new ConcurrentHashMap<>(); //Cache of the json data awaiting save
	private static final List<Filename> FILENAMES = new ArrayList<>(); //Static reference to currently used filenames

	private static final Logger LOGGER = LoggerFactory.getLogger(JSON.class);
	private static final String FOLDER = "data";
	private static final Configuration CONFIGURATION = Configuration.defaultConfiguration();

	static
	{
		FILENAMES.addAll(Arrays.asList(Filename.values()));
		FILENAMES.remove(Filename.ALL);

		CONFIGURATION.addOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);
	}

	public static void createFiles()
	{
		File folder = new File(FOLDER);
		if(!folder.exists())
		{
			folder.mkdir();
		}
		for(Filename file : FILENAMES)
		{
			try
			{
				File json = new File(folder, file + ".json");
				json.createNewFile();
				FILES.put(file, json);
			}
			catch(Exception exception)
			{
				LOGGER.error("A JSON error occurred.", exception);
			}
		}
	}

	public static void loadFile(Filename filename)
	{
		if(filename.equals(Filename.ALL))
		{
			for(Filename file : FILENAMES)
			{
				try
				{
					JSON_CACHE.put(file, JsonPath.using(CONFIGURATION).parse(FILES.get(file)));
				}
				catch(Exception exception)
				{
					LOGGER.error("A JSON error occurred.", exception);
				}
			}
		}
		else
		{
			try
			{
				JSON_CACHE.put(filename, JsonPath.using(CONFIGURATION).parse(FILES.get(filename)));
			}
			catch(Exception exception)
			{
				LOGGER.error("A JSON error occurred.", exception);
			}
		}
	}

	public static void saveFile(Filename filename)
	{
		if(filename.equals(Filename.ALL))
		{
			for(Filename fileName : FILENAMES)
			{
				try
				{
					FileWriter writer = new FileWriter(new File(FOLDER, fileName + ".json"));
					writer.write(JSON_CACHE.get(fileName).jsonString());
					writer.close();
				}
				catch(Exception ignored)
				{

				}
			}
		}
		else
		{
			try
			{
				FileWriter writer = new FileWriter(new File(FOLDER, filename + ".json"));
				writer.write(JSON_CACHE.get(filename).jsonString());
				writer.close();
			}
			catch(Exception ignored)
			{

			}
		}
	}


	public static String getFieldString(String path, Filename filename)
	{
		path = "$." + path;
		DocumentContext context = JSON_CACHE.get(filename);
		return context.read(path);
	}

	public static int getFieldInt(String path, Filename filename)
	{
		path = "$." + path;
		DocumentContext context = JSON_CACHE.get(filename);
		String content = context.read(path);
		try
		{
			return Integer.parseInt(content);
		}
		catch(Exception exception)
		{
			return -1;
		}
	}

	public static Boolean getFieldBool(String path, Filename filename)
	{
		path = "$." + path;
		DocumentContext context = JSON_CACHE.get(filename);
		String content = context.read(path);
		try
		{
			return Boolean.parseBoolean(content);
		}
		catch(Exception exception)
		{
			return null;
		}
	}

	public static void updateField(String path, Filename filename, Object data)
	{
		path = "$." + path;
		JsonPath jsonPath = JsonPath.compile(path);
		DocumentContext context = JSON_CACHE.get(filename);
		if(context != null)
		{
			context.set(jsonPath, data);
			JSON_CACHE.put(filename, context);
		}

	}

	public static void addFieldDefault(String path, String key, Filename filename, String defaultValue)
	{
		path = "$." + path;
		DocumentContext context = JSON_CACHE.get(filename);
		JsonPath jsonPath = JsonPath.compile(path);
		if(context != null)
		{
			try
			{
				context.read(jsonPath);
			}
			catch(Exception exception)
			{
				context.put(jsonPath, key, defaultValue);
			}
			JSON_CACHE.put(filename, context);
		}
	}

	public static void applyDefaults()
	{
		addFieldDefault("mysql", "username", Filename.CONFIG, "username");
		addFieldDefault("mysql", "password", Filename.CONFIG, "password");
		addFieldDefault("mysql", "database", Filename.CONFIG, "jdbc:mysql://localhost:3306/database?useSSL=false");

		addFieldDefault("bot", "token", Filename.CONFIG, "null");
		addFieldDefault("bot", "server", Filename.CONFIG, "null");
		addFieldDefault("bot","error", Filename.CONFIG, "null");
		addFieldDefault("bot","privileged",  Filename.CONFIG, "null");

		addFieldDefault("ranks","default", Filename.MINECRAFT , "null");

		addFieldDefault("ranks","rising", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","flying", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","soaring", Filename.MINECRAFT, "null");

		addFieldDefault("ranks","epic", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","epic2", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","epic3", Filename.MINECRAFT, "null");

		addFieldDefault("ranks","elite", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","elite2", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","elite3", Filename.MINECRAFT, "null");

		addFieldDefault("ranks","mod", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","mod2", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","mod3", Filename.MINECRAFT, "null");

		addFieldDefault("ranks","council", Filename.MINECRAFT, "null");

		addFieldDefault("ranks","birthday", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","nitroboost", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","founder", Filename.MINECRAFT, "null");
		addFieldDefault("ranks","retired", Filename.MINECRAFT, "null");
		addFieldDefault("ranks", "developer",Filename.MINECRAFT, "null");

		saveFile(Filename.ALL);
	}
}
