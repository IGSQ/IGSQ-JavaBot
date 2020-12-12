package org.igsq.igsqbot;

import java.io.File;

import org.igsq.igsqbot.handlers.ErrorHandler;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;


public class Yaml
{
	private Yaml()
	{
		// To override the default, public, constructor
	}
    /**
     * filenames is a String array of all of the fileNames to be created into {@link java.io.File}
     * @apiNote Used in {@link #createFiles()} to instantiate the filenames.
     * @see java.io.File
     */
    private static final String[] fileNames = {"config","internal","guild","verification","minecraft", "punishment"};
    /**
     * files is a {@link java.io.File File} array of all of the files that can be used.
     * @apiNote Used in {@link #loadFile(String)} to get data from file & in {@link #saveFileChanges(String)} to save data to file
     */
    private static File[] files;
    /**
     * configurations is an array of all of the cached file contents to be saved onto file {@link #files file}.
     * @apiNote Used in {@link #getFieldString(String, String) getting} & {@link #updateField(String, String, Object) setting} file contents as well as {@link #loadFile(String) loading} & {@link #saveFileChanges(String) saving}.
     * @see java.io.File
     */
    private static FileConfiguration[] configurations;
    /**
     * Creates all the files if they don't already exist. Creates instance of all files in {@link #fileNames}
     * @apiNote also creates default {@link #configurations}
     * @see java.io.File
     */
    
    public static void createFiles() 
    {
		 try
         {
         	File folder = new File("data");
	    	if (!folder.exists())
		    {
			    folder.mkdir();
		    }
	         files = new File[fileNames.length];
	         configurations = new YamlConfiguration[fileNames.length];
			 for (int i = 0; i < fileNames.length; i++)
			 {
				 files[i] = new File(folder, fileNames[i] + ".yml");
				 files[i].createNewFile();
			 }
         }
         catch (Exception exception)
		 {
        	 new ErrorHandler(exception);
		 }
    	
    }

    public static void addFieldDefault(String path,String fileName,Object data)
	{
		for (int i = 0; i < fileNames.length; i++)
		{
			if (fileNames[i].equalsIgnoreCase(fileName))
			{
				configurations[i].addDefault(path, data);
				break;
			}
		}
	}

    public static String getFieldString(String path,String fileName) 
    {
    	for(int i = 0; i < fileNames.length;i++) 
    	{
    		if(fileNames[i].equalsIgnoreCase(fileName)) 
    		{
    			return configurations[i].getString(path);
    		}
    	}
    	return null;
    }

    public static Boolean getFieldBool(String path,String fileName) 
    {
    	for(int i = 0; i < fileNames.length;i++) 
    	{
    		if(fileNames[i].equalsIgnoreCase(fileName)) 
    		{
    			return configurations[i].getBoolean(path);
    		}
    	}
    	return false;
    }

    public static int getFieldInt(String path,String fileName) 
    {
    	for(int i = 0; i < fileNames.length;i++) 
    	{
    		if(fileNames[i].equalsIgnoreCase(fileName)) 
    		{
    			return configurations[i].getInt(path);
    		}
    	}
    	return -1;
    }

    public static void updateField(String path,String fileName,Object data) 
    {
    	for(int i = 0; i < fileNames.length;i++) 
    	{
    		if(fileNames[i].equalsIgnoreCase(fileName))
    		{
    			configurations[i].set(path, data);
    			break;
    		}
    	}
    }
    public static void loadFile(String fileName) 
    {
    	try 
    	{
        	for(int i = 0; i < fileNames.length;i++) 
        	{
        		if(fileName.equalsIgnoreCase("@all")) 
        		{
        			configurations[i] = new YamlConfiguration();
        			configurations[i].load(files[i]);
        		}
        		else if(fileNames[i].equalsIgnoreCase(fileName))
        		{
        			configurations[i] = new YamlConfiguration();
        			configurations[i].load(files[i]);
        			break;
        		}
        	}
		}
    	catch (Exception exception)
    	{
    		new ErrorHandler(exception);
		}
    }
    public static void saveFileChanges(String fileName) 
    {
		try 
		{
	    	for(int i = 0; i < fileNames.length;i++) 
			{
				if(fileName.equalsIgnoreCase("@all")) 
				{
					configurations[i].save(files[i]);
				}
				else if(fileNames[i].equalsIgnoreCase(fileName))
				{
					configurations[i].save(files[i]);
					break;
				}
			}
		}
		catch (Exception exception)
		{
			new ErrorHandler(exception);
		}
    }

    public static void disregardAndCloseFile(String fileName)
    {
    	for(int i = 0; i < fileNames.length;i++) 
    	{
    		if(fileName.equalsIgnoreCase("@all")) 
    		{
    			configurations[i] = null;
    			files[i] = null;
    		}
    		else if(fileNames[i].equalsIgnoreCase(fileName))
    		{
    			configurations[i] = null;
    			files[i] = null;
    			break;
    		}
    	}
    }
    
    public static void applyDefault()
    {
        addFieldDefault("MYSQL","config",true);
        addFieldDefault("MYSQL.username","config","username");
        addFieldDefault("MYSQL.password","config","password");
        addFieldDefault("MYSQL.database","config","jdbc:mysql://localhost:3306/database?useSSL=false");
        
        addFieldDefault("BOT.token","config","token");
        addFieldDefault("BOT.server","config","");
        
        addFieldDefault("ranks.default", "minecraft", "");
        
        addFieldDefault("ranks.rising", "minecraft", "");
        addFieldDefault("ranks.flying", "minecraft", "");
        addFieldDefault("ranks.soaring", "minecraft", "");
        
        addFieldDefault("ranks.epic", "minecraft", "");
        addFieldDefault("ranks.epic2", "minecraft", "");
        addFieldDefault("ranks.epic3", "minecraft", "");
        
        addFieldDefault("ranks.elite", "minecraft", "");
        addFieldDefault("ranks.elite2", "minecraft", "");
        addFieldDefault("ranks.elite3", "minecraft", "");
        
        addFieldDefault("ranks.mod", "minecraft", "");
        addFieldDefault("ranks.mod2", "minecraft", "");
        addFieldDefault("ranks.mod3", "minecraft", "");
        
        addFieldDefault("ranks.council", "minecraft", "");
        
        addFieldDefault("ranks.birthday", "minecraft", "");
        addFieldDefault("ranks.nitroboost", "minecraft", "");
        addFieldDefault("ranks.founder", "minecraft", "");
        addFieldDefault("ranks.retired", "minecraft", "");
        addFieldDefault("ranks.developer", "minecraft", "");

        for(FileConfiguration configuration : configurations) configuration.options().copyDefaults(true);
    }

}
