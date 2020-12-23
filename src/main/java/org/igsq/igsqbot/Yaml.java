package org.igsq.igsqbot;

import org.igsq.igsqbot.entities.yaml.Filename;
import org.igsq.igsqbot.handlers.ErrorHandler;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;


public class Yaml
{
	/**
	 * filenames is a String array of all of the FILE_NAMES to be created into {@link java.io.File}
	 *
	 * @apiNote Used in {@link #createFiles()} to instantiate the filenames.
	 * @see java.io.File
	 */
	private static final Filename[] FILE_NAMES = Filename.values();
	/**
	 * files is a {@link java.io.File File} array of all of the files that can be used.
	 *
	 * @apiNote Used in {@link #loadFile(String)} to get data from file & in {@link #saveFileChanges(String)} to save data to file
	 */
	private static File[] files;
	/**
	 * configurations is an array of all of the cached file contents to be saved onto file {@link #files file}.
	 *
	 * @apiNote Used in {@link #getFieldString(String, String) getting} & {@link #updateField(String, String, Object) setting} file contents as well as {@link #loadFile(String) loading} & {@link #saveFileChanges(String) saving}.
	 * @see java.io.File
	 */
	private static FileConfiguration[] configurations;
	private Yaml()
	{
		// To override the default, public, constructor
	}

	/**
	 * Creates all the files if they don't already exist. Creates instance of all files in {@link #FILE_NAMES}
	 *
	 * @apiNote also creates default {@link #configurations}
	 * @see java.io.File
	 */

	public static void createFiles()
	{
		try
		{
			File folder = new File("data");
			if(!folder.exists())
			{
				folder.mkdir();
			}
			files = new File[FILE_NAMES.length];
			configurations = new YamlConfiguration[FILE_NAMES.length];
			for(int i = 0; i < FILE_NAMES.length; i++)
			{
				files[i] = new File(folder, FILE_NAMES[i] + ".yml");
				files[i].createNewFile();
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}

	}

	public static void addFieldDefault(String path, Filename fileName, Object data)
	{
		for(int i = 0; i < FILE_NAMES.length; i++)
		{
			if(FILE_NAMES[i].equals(fileName))
			{
				configurations[i].addDefault(path, data);
				break;
			}
		}
	}

	public static String getFieldString(String path, Filename fileName)
	{
		for(int i = 0; i < FILE_NAMES.length; i++)
		{
			if(FILE_NAMES[i].equals(fileName))
			{
				return configurations[i].getString(path);
			}
		}
		return "";
	}

	public static Boolean getFieldBool(String path, Filename fileName)
	{
		for(int i = 0; i < FILE_NAMES.length; i++)
		{
			if(FILE_NAMES[i].equals(fileName))
			{
				return configurations[i].getBoolean(path);
			}
		}
		return false;
	}

	public static int getFieldInt(String path, Filename fileName)
	{
		for(int i = 0; i < FILE_NAMES.length; i++)
		{
			if(FILE_NAMES[i].equals(fileName))
			{
				return configurations[i].getInt(path);
			}
		}
		return -1;
	}

	public static void updateField(String path, Filename fileName, Object data)
	{
		for(int i = 0; i < FILE_NAMES.length; i++)
		{
			if(FILE_NAMES[i].equals(fileName))
			{
				configurations[i].set(path, data);
				break;
			}
		}
	}

	public static void loadFile(Filename fileName)
	{
		try
		{
			for(int i = 0; i < FILE_NAMES.length; i++)
			{
				if(fileName.equals(Filename.ALL))
				{
					configurations[i] = new YamlConfiguration();
					configurations[i].load(files[i]);
				}
				else if(FILE_NAMES[i].equals(fileName))
				{
					configurations[i] = new YamlConfiguration();
					configurations[i].load(files[i]);
					break;
				}
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static void saveFileChanges(Filename fileName)
	{
		try
		{
			for(int i = 0; i < FILE_NAMES.length; i++)
			{
				if(fileName.equals(Filename.ALL))
				{
					configurations[i].save(files[i]);
				}
				else if(FILE_NAMES[i].equals(fileName))
				{
					configurations[i].save(files[i]);
					break;
				}
			}
		}
		catch(Exception exception)
		{
			new ErrorHandler(exception);
		}
	}

	public static void disregardAndCloseFile(Filename fileName)
	{
		for(int i = 0; i < FILE_NAMES.length; i++)
		{
			if(fileName.equals(Filename.ALL))
			{
				configurations[i] = null;
				files[i] = null;
			}
			else if(FILE_NAMES[i].equals(fileName))
			{
				configurations[i] = null;
				files[i] = null;
				break;
			}
		}
	}

	public static void applyDefault()
	{
		addFieldDefault("mysql", Filename.CONFIG, true);
		addFieldDefault("mysql.username", Filename.CONFIG, "username");
		addFieldDefault("mysql.password", Filename.CONFIG, "password");
		addFieldDefault("mysql.database", Filename.CONFIG, "jdbc:mysql://localhost:3306/database?useSSL=false");

		addFieldDefault("bot.token", Filename.CONFIG, "token");
		addFieldDefault("bot.server", Filename.CONFIG, "");
		addFieldDefault("bot.error", Filename.CONFIG, "");

		addFieldDefault("ranks.default", Filename.MINECRAFT , "");

		addFieldDefault("ranks.rising", Filename.MINECRAFT, "");
		addFieldDefault("ranks.flying", Filename.MINECRAFT, "");
		addFieldDefault("ranks.soaring", Filename.MINECRAFT, "");

		addFieldDefault("ranks.epic", Filename.MINECRAFT, "");
		addFieldDefault("ranks.epic2", Filename.MINECRAFT, "");
		addFieldDefault("ranks.epic3", Filename.MINECRAFT, "");

		addFieldDefault("ranks.elite", Filename.MINECRAFT, "");
		addFieldDefault("ranks.elite2", Filename.MINECRAFT, "");
		addFieldDefault("ranks.elite3", Filename.MINECRAFT, "");

		addFieldDefault("ranks.mod", Filename.MINECRAFT, "");
		addFieldDefault("ranks.mod2", Filename.MINECRAFT, "");
		addFieldDefault("ranks.mod3", Filename.MINECRAFT, "");

		addFieldDefault("ranks.council", Filename.MINECRAFT, "");

		addFieldDefault("ranks.birthday", Filename.MINECRAFT, "");
		addFieldDefault("ranks.nitroboost", Filename.MINECRAFT, "");
		addFieldDefault("ranks.founder", Filename.MINECRAFT, "");
		addFieldDefault("ranks.retired", Filename.MINECRAFT, "");
		addFieldDefault("ranks.developer", Filename.MINECRAFT, "");

		for(FileConfiguration configuration : configurations) configuration.options().copyDefaults(true);
	}

}
