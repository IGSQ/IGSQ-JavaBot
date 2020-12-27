package org.igsq.igsqbot.util;

import org.igsq.igsqbot.Yaml;
import org.igsq.igsqbot.entities.yaml.Filename;

public class YamlUtils
{
	private YamlUtils()
	{
		//Overrides the default, public constructor
	}

	public static boolean isFieldEmpty(String path, Filename filename)
	{
		return Yaml.getFieldString(path, filename) == null || Yaml.getFieldString(path, filename).isEmpty();
	}

	public static String getFieldAppended(String path, Filename filename, String delimiter, Object data)
	{
		if(isFieldEmpty(path, filename))
		{
			return data + delimiter;
		}
		else
		{
			String onFile = Yaml.getFieldString(path, filename);

			while(onFile.startsWith(delimiter))
			{
				onFile = onFile.substring(0, delimiter.length());
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

	public static void clearField(String path, Filename filename)
	{
		Yaml.updateField(path, filename, null);
	}
}
