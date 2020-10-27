package org.igsq.igsqbot;

import java.io.File;
import java.io.IOException;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import net.dv8tion.jda.api.entities.Guild;

public class Yaml 
{
    /**
     * filenames is a String array of all of the fileNames to be created into {@link java.io.File}
     * @apiNote Used in {@link #createFiles()} to instansiate the filesnames.
     * @see java.io.File
     */
    public static String[] fileNames = {"config","internal","guild"};
    /**
     * files is a {@link java.io.File File} array of all of the files that can be used.
     * @apiNote Used in {@link #loadFile(String)} to get data from file & in {@link #saveFileChanges(String)} to save data to file
     */
    private static File[] files;
    /**
     * configurations is a {@link org.bukkit.configuration.file.FileConfiguration FileConfiguration} array of all of the cached file contents to be saved onto file {@link #files file}.
     * @apiNote Used in {@link #getFieldString(String, String) getting} & {@link #updateField(String, String, String) setting} file contents aswell as {@link #loadFile(String) loading} & {@link #saveFileChanges(String) saving}.
     * @see java.io.File
     */
    private static FileConfiguration[] configurations;
    
    
    
    /**
     * Creates all the files if they dont already exist. Creates instance of all files in {@link #fileNames}
     * @apiNote also creates default {@link #configurations}
     * @see java.io.File
     * @see org.bukkit.configuration.file.FileConfiguration
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
	    	for (int i = 0; i < fileNames.length;i++) 
	    	{
	    		files[i] = new File(folder,fileNames[i] + ".yml");
	    		files[i].createNewFile();
	    		
	    	}
	    	
         }
         catch (Exception exception)
		 {
        	 
		 }
    	
    }
    //TODO Java Docs
    public static void addFieldDefault(String path,String fileName,Object data) 
    {
    	for(int i = 0; i < fileNames.length;i++) 
    	{
    		if(fileNames[i].equalsIgnoreCase(fileName)) 
    		{
    			configurations[i].addDefault(path, data);
    			break;
    		}
    	}
    }
    //TODO Java Docs
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
    //TODO Java Docs
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
    //TODO Java Docs
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
    
    //TODO Java Docs
    public static void updateField(String path,String fileName,String data) 
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
    public static void updateField(String path,String fileName,Boolean data) 
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
    //TODO Java Docs
    public static void updateField(String path,String fileName,int data) 
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
  //TODO Java Docs
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
    	catch (Exception e)
    	{
    		
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
		catch (IOException e) 
		{
			
		}
    }
    //TODO Java Docs
    public static void disgardAndCloseFile(String fileName) 
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
        
        for(Guild selectedGuild : Common.jda.getGuilds())
        {
        	addFieldDefault(selectedGuild.getId(),"guild",true);
        	
        	addFieldDefault(selectedGuild.getId() + ".basiclogchannel", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".moderatorlogchannel", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".administratorlogchannel", "guild", "none");
        	
        	addFieldDefault(selectedGuild.getId() + ".voicelogchannel", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".membercountchannel", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".votingchannel", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".suggestionschannel", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".verificationchannel", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".reportchannel", "guild", "none");
        	
        	addFieldDefault(selectedGuild.getId() + ".verifiedrole", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".moderatorrole", "guild", "none");
        	addFieldDefault(selectedGuild.getId() + ".administratorrole", "guild", "none");
        }
        for(FileConfiguration configuration : configurations) configuration.options().copyDefaults(true);
    }

}
