package org.igsq.igsqbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.igsq.igsqbot.entities.json.Filename;
import org.igsq.igsqbot.entities.json.IJsonEntity;
import org.igsq.igsqbot.entities.json.BotConfig;
import org.igsq.igsqbot.entities.json.MinecraftConfig;
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
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			gson.toJson(json, writer);
			writer.close();
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static void updateFile(IJsonEntity json, Filename filename)
	{
		try
		{
			Writer writer = new FileWriter(new File(FOLDER, filename + ".json"));
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
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

	public static String parseUnicode(String input)
	{
		StringBuilder builder = new StringBuilder();
		input.codePoints().forEachOrdered(code ->
		{
			char[] chars = Character.toChars(code);
			if(chars.length > 1)
			{
				String hex0 = Integer.toHexString(chars[0]).toUpperCase();
				String hex1 = Integer.toHexString(chars[1]).toUpperCase();
				while(hex0.length() < 4)
				{
					hex0 = "0" + hex0;
				}

				while(hex1.length() < 4)
				{
					hex1 = "0" + hex1;
				}
				builder.append("\\u").append(hex0).append("\\u").append(hex1);
			}
		});
		return builder.toString();
	}

	public static void applyDefaults()
	{
		addDefault(new BotConfig().toJson(), Filename.CONFIG);
		addDefault(new MinecraftConfig().toJson(), Filename.MINECRAFT);
	}
}
