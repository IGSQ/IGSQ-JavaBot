package org.igsq.igsqbot.util;

import org.igsq.igsqbot.Yaml;

public class YamlUtils
{
	private YamlUtils()
	{
		//Overrides the default, public constructor
	}

	public static boolean isFieldEmpty(String path, String filename)
	{
		return Yaml.getFieldString(path, filename) == null || Yaml.getFieldString(path, filename).isEmpty();
	}

	public static String fieldAppend(String path, String filename, String delimiter, Object data)
	{
		if(isFieldEmpty(path, filename))
		{
			return data + delimiter;
		}
		else
		{
			String onFile = Yaml.getFieldString(path, filename);

			onFile = onFile.strip();
			while(onFile.startsWith(delimiter))
			{
				onFile = onFile.substring(0, 1);
			}

			if(onFile.endsWith(delimiter))
			{
				return onFile + data;
			}
			else
			{
				return onFile + delimiter + data;
			}
		}
	}

	public static void clearField(String path, String filename)
	{
		Yaml.updateField(path, filename, null);
	}
}
