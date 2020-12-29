package org.igsq.igsqbot.util;

import net.dv8tion.jda.api.entities.Icon;

import java.io.InputStream;
import java.net.URL;

public class FileUtils
{
	private FileUtils()
	{
		//Overrides the default, public, constructor
	}

	public static InputStream getResourceFile(String fileName)
	{
		InputStream file;
		try
		{
			file = FileUtils.class.getResource(fileName).toURI().toURL().openStream();
		}
		catch(Exception exception)
		{
			return null;
		}
		return file;
	}

	public static Icon getIcon(String url)
	{
		Icon icon;
		try
		{
			icon = Icon.from(new URL(url).openStream());

		}
		catch(Exception exception)
		{
			return null;
		}
		return icon;
	}
}
