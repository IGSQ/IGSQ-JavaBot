package org.igsq.igsqbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.IJson;
import org.igsq.igsqbot.entities.json.JsonBotConfig;
import org.igsq.igsqbot.entities.json.JsonMinecraft;
import org.igsq.igsqbot.handlers.ErrorHandler;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Json
{
	private Json()
	{
		//Overrides the default, public, constructor
	}
	private static final List<Filename> FILENAMES = new ArrayList<>(); //Static reference to currently used filenames
	public static final String FOLDER = "data";

	static
	{
		FILENAMES.addAll(Arrays.asList(Filename.values()));
		FILENAMES.remove(Filename.ALL);
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
			}
			catch(Exception exception)
			{
				new ErrorHandler(exception);
			}
		}
	}

	public static void updateFile(Object json, Filename filename)
	{
		try
		{
			Writer writer = new FileWriter(new File(FOLDER, filename + ".json"));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(json, writer);
			writer.close();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static void updateFile(IJson json, Filename filename)
	{
		try
		{
			Writer writer = new FileWriter(new File(FOLDER, filename + ".json"));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(json.toJson(), writer);
			writer.close();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static <T> T get(Class<T> type, Filename filename)
	{
		try
		{
			return new Gson().fromJson(new FileReader(FOLDER + "/" + filename + ".json"), type);
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
			return null;
		}
	}

	public static void addDefault(JsonObject json, Filename filename)
	{
		try
		{
			JsonObject onFile = new Gson().fromJson(new FileReader(FOLDER + "/" + filename + ".json"), JsonObject.class);
			JsonObject fileAppended = new JsonObject();

			if(onFile != null)
			{
				onFile.entrySet().forEach(entry ->  fileAppended.add(entry.getKey(), entry.getValue()));
				json.entrySet().forEach(entry ->
				{
					if(!onFile.has(entry.getKey()))
					{
						fileAppended.add(entry.getKey(), entry.getValue());
					}
				});
				if(!fileAppended.toString().equals(""))
				{
					updateFile(fileAppended, filename);
				}
			}
			else
			{
				updateFile(json, filename);
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static void applyDefaults()
	{
		addDefault(new JsonBotConfig().toJson(), Filename.CONFIG);
		addDefault(new JsonMinecraft().toJson(), Filename.MINECRAFT);
	}
}
